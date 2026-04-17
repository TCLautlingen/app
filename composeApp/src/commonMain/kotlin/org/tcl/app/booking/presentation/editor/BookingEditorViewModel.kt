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
import org.tcl.app.booking.domain.BookingRepository
import kotlin.math.abs
import kotlin.time.Clock

class BookingEditorViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val repository: BookingRepository
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
    }

    fun onAction(action: BookingEditorAction) {
        when (action) {
            is BookingEditorAction.OnDateChange -> {
                savedStateHandle["date"] = action.date.toString()
                _state.update { it.copy(date = action.date) }
                loadAvailability()
            }
            is BookingEditorAction.OnDurationChange -> {
                savedStateHandle["duration"] = action.duration
                _state.update { it.copy(duration = action.duration) }
                loadAvailability()
            }
            is BookingEditorAction.OnStartTimeChange -> {
                savedStateHandle["startTime"] = action.startTime.toString()
                _state.update {
                    it.copy(
                        startTime = action.startTime,
                        courtId = it.availableSlots
                            .filter { slot -> LocalTime.parse(slot.startTime) == action.startTime }
                            .map { slot -> slot.courtId }
                            .distinct()
                            .firstOrNull()
                    )
                }
            }
            is BookingEditorAction.OnCourtChange -> {
                savedStateHandle["courtId"] = action.courtId
                _state.update { it.copy(courtId = action.courtId) }
            }
            is BookingEditorAction.OnBookClick -> bookCourt()
        }
    }

    private fun loadAvailability() {
        val date = _state.value.date.toString()
        val duration = _state.value.duration
        if (duration == 0) return

        viewModelScope.launch {
            val slots = repository.getAvailableSlots(date, duration)
            _state.update { it ->
                it.copy(
                    availableSlots = slots,
                )
            }

            val availableTimes = slots.map { LocalTime.parse(it.startTime) }.distinct()
            val selectedStartTime = _state.value.startTime

            if (selectedStartTime !in availableTimes) {
                val currentStartTime = currentTimeRounded()
                val closestTime = availableTimes.minByOrNull { currentStartTime.minutesTo(it) }
                _state.update { it.copy(startTime = closestTime) }
            }

            val availableCourts = slots
                .filter { LocalTime.parse(it.startTime) == _state.value.startTime }
                .map { it.courtId }

            if (_state.value.courtId !in availableCourts) {
                _state.update { it.copy(courtId = availableCourts.firstOrNull()) }
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
            repository.createBooking(
                courtId = currentState.courtId,
                date = currentState.date,
                startTime = currentState.startTime,
                duration = currentState.duration
            )
            _state.update { it.copy(isSaving = false) }
            _events.send(BookingEditorEvent.CourtBooked)
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
    val roundedHour = if (now.minute < 30) now.hour else now.hour + 1
    return LocalTime(roundedHour, roundedMinute)
}