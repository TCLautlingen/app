package org.tcl.app.booking.presentation.editor

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kizitonwose.calendar.core.now
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.tcl.app.booking.domain.BookingRemoteDataSource
import org.tcl.app.core.domain.util.onFailure
import org.tcl.app.core.domain.util.onSuccess
import org.tcl.app.user.domain.UserRemoteDataSource
import kotlin.collections.contains
import kotlin.math.abs
import kotlin.time.Clock

class BookingEditorViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val dataSource: BookingRemoteDataSource,
    private val userRemoteDataSource: UserRemoteDataSource
) : ViewModel() {
    private val _state = MutableStateFlow(BookingEditorState(
        date = LocalDate.parse(savedStateHandle["date"] ?: LocalDate.now().toString()),
        courtId = savedStateHandle["courtId"] ?: 1,
        startTime = savedStateHandle["startTime"] ?: currentTimeRounded(),
        duration = savedStateHandle["duration"] ?: 60,
    ))
    val state = _state.asStateFlow()

    private val _events = Channel<BookingEditorEvent>()
    val events = _events.receiveAsFlow()

    init {
        loadAvailability()
        viewModelScope.launch {
            userRemoteDataSource.getCurrentUser()
                .onSuccess { user ->
                    _state.update { it.copy(currentUserId = user.id) }
                }
        }
    }

    fun initialize(date: LocalDate?, court: Int?, startTime: LocalTime?) {
        if (date != null) {
            savedStateHandle["date"] = _state.value.date.toString()
            _state.update { it.copy(date = date) }
        }
        if (court != null) {
            savedStateHandle["courtId"] = _state.value.courtId
            _state.update { it.copy(courtId = court) }
        }
        if (startTime != null) {
            savedStateHandle["startTime"] = _state.value.startTime.toString()
            _state.update { it.copy(startTime = startTime) }
        }
        loadAvailability()
        loadPlayers(_state.value.playerSearchQuery)
    }

    fun onAction(action: BookingEditorAction) {
        when (action) {
            is BookingEditorAction.OnDateClick -> _state.update { it.copy(showDateSheet = true) }
            is BookingEditorAction.OnDateChange -> {
                savedStateHandle["date"] = action.date.toString()
                _state.update { it.copy(date = action.date, showDateSheet = false) }
                loadAvailability()
            }
            is BookingEditorAction.OnDateChangeDismiss -> _state.update { it.copy(showDateSheet = false) }
            is BookingEditorAction.OnDurationChange -> {
                savedStateHandle["duration"] = action.duration
                _state.update { it.copy(duration = action.duration) }
                loadAvailability()
            }
            is BookingEditorAction.OnStartTimeChange -> {
                savedStateHandle["startTime"] = action.startTime.toString()
                _state.update {
                    it.copy(
                        startTime = action.startTime
                    )
                }
                val availableCourts = _state.value.availableSlots
                    .filter { LocalTime.parse(it.startTime) == _state.value.startTime }
                    .map { it.court.id }

                if (_state.value.courtId !in availableCourts) {
                    savedStateHandle["courtId"] = availableCourts.firstOrNull()
                    _state.update { it.copy(courtId = availableCourts.firstOrNull()) }
                }
            }
            is BookingEditorAction.OnCourtChange -> {
                savedStateHandle["courtId"] = action.courtId
                _state.update { it.copy(courtId = action.courtId) }
            }
            is BookingEditorAction.OnBookClick -> bookCourt()
            is BookingEditorAction.OnAddPlayerClick -> {
                _state.update { it.copy(showPlayerSelectSheet = true) }
            }
            is BookingEditorAction.OnPlayerSheetDismiss -> {
                _state.update { it.copy(showPlayerSelectSheet = false) }
            }
            is BookingEditorAction.OnPlayerToggle -> {
                _state.update {
                    val updatedIds = if (action.user.id in it.selectedPlayerIds) {
                        it.selectedPlayerIds - action.user.id
                    } else {
                        it.selectedPlayerIds + action.user.id
                    }
                    it.copy(selectedPlayerIds = updatedIds)
                }
            }
            is BookingEditorAction.OnPlayerSearchChange -> {
                _state.update { it.copy(playerSearchQuery = action.query) }
                loadPlayers(action.query)
            }
        }
    }

    private fun loadAvailability() {
        val date = _state.value.date.toString()
        val duration = _state.value.duration
        if (duration == 0) return

        viewModelScope.launch {
            dataSource.getAvailableSlots(date, duration)
                .onSuccess { slots ->
                    _state.update { it.copy(availableSlots = slots) }

                    val availableTimes = slots.map { LocalTime.parse(it.startTime) }.distinct()
                    val selectedStartTime = _state.value.startTime

                    if (selectedStartTime !in availableTimes) {
                        val currentStartTime = currentTimeRounded()
                        val closestTime = availableTimes.minByOrNull { currentStartTime.minutesTo(it) }
                        _state.update { it.copy(startTime = closestTime) }
                    }

                    val availableCourts = slots
                        .filter { LocalTime.parse(it.startTime) == _state.value.startTime }
                        .map { it.court.id }

                    if (_state.value.courtId !in availableCourts) {
                        _state.update { it.copy(courtId = availableCourts.firstOrNull()) }
                    }
                }
                .onFailure {

                }
        }
    }

    private fun loadPlayers(query: String) {
        viewModelScope.launch {
            userRemoteDataSource.getUsers(query)
                .onSuccess { users ->
                    val filtered = users.filter { it.id != _state.value.currentUserId }
                    _state.update { it.copy(players = filtered) }
                }
                .onFailure {

                }
        }
    }

    private fun bookCourt() {
        val currentState = _state.value

        if (currentState.startTime == null || currentState.courtId == null) {
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isSaving = true) }
            dataSource.createBooking(
                courtId = currentState.courtId,
                date = currentState.date,
                startTime = currentState.startTime,
                duration = currentState.duration,
                playerIds = currentState.selectedPlayerIds
            )
                .onSuccess { booking ->
                    _state.update { it.copy(isSaving = false) }
                    _events.send(
                        BookingEditorEvent.CourtBooked(booking)
                    )
                }
                .onFailure {
                    _state.update { it.copy(isSaving = false) }
                }
        }
    }
}

private fun LocalTime.minutesTo(t2: LocalTime): Int {
    val minutes1 = this.hour * 60 + this.minute
    val minutes2 = t2.hour * 60 + t2.minute
    return abs(minutes1 - minutes2)
}

fun currentTimeRounded(): LocalTime {
    val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
    val roundedMinute = if (now.minute < 30) 30 else 0
    val roundedHour = if (now.minute < 30) now.hour else (now.hour + 1) % 24
    return LocalTime(roundedHour, roundedMinute)
}