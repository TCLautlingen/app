package org.tcl.app.booking.presentation.editor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.tcl.app.Booking
import org.tcl.app.booking.domain.BookingRepository
import kotlin.math.abs
import kotlin.time.Clock

class BookingEditorViewModel(
    private val repository: BookingRepository = BookingRepository()
) : ViewModel() {
    private val _state = MutableStateFlow(BookingEditorState())
    val state = _state.asStateFlow()

    private val _events = Channel<BookingEditorEvent>()
    val events = _events.receiveAsFlow()

    init {
        loadAvailability()
    }

    fun onAction(action: BookingEditorAction) {
        when (action) {
            is BookingEditorAction.OnDateChange -> {
                _state.update { it.copy(date = action.date) }
                loadAvailability()
            }
            is BookingEditorAction.OnDurationChange -> {
                _state.update { it.copy(duration = action.duration) }
                loadAvailability()
            }
            is BookingEditorAction.OnStartTimeChange -> {
                _state.update {
                    it.copy(
                        startTime = action.startTime,
                        court = it.availableSlots
                            .filter { slot -> slot.startTime == action.startTime }
                            .map { slot -> slot.court }
                            .distinct()
                            .firstOrNull()
                    )
                }
            }
            is BookingEditorAction.OnCourtChange -> {
                _state.update { it.copy(court = action.court) }
            }
            is BookingEditorAction.OnBookClick -> bookCourt()
        }
    }

    private fun loadAvailability() {
        val date = _state.value.date.toString()
        val duration = _state.value.duration
        if (duration == 0) return

        viewModelScope.launch {
            val slots = repository.getAvailability(date, duration)
            _state.update { it ->
                it.copy(
                    availableSlots = slots,
                )
            }

            val availableTimes = slots.map { it.startTime }.distinct()
            val selectedStartTime = _state.value.startTime

            if (selectedStartTime !in availableTimes) {
                val currentStartTime = currentTimeRounded()
                val closestTime = availableTimes.minByOrNull { timeDiff(currentStartTime, it) }
                _state.update { it.copy(startTime = closestTime) }
            }

            val availableCourt = slots
                .filter { it.startTime == _state.value.startTime }
                .map { it.court }
                .firstOrNull()

            _state.update { it.copy(court = availableCourt) }


        }
    }

    private fun bookCourt() {
        val currentState = _state.value

        if (currentState.startTime == null || currentState.court == null) {
            return
        }

        val booking = Booking(
            id = "",
            date = currentState.date.toString(),
            startTime = currentState.startTime,
            duration = currentState.duration,
            court = currentState.court,
        )

        viewModelScope.launch {
            _state.update { it.copy(isSaving = true) }
            repository.addBooking(booking)
            _state.update { it.copy(isSaving = false) }
            _events.send(BookingEditorEvent.CourtBooked)
        }
    }
}

fun timeDiff(t1: String, t2: String): Int {
    val (h1, m1) = t1.split(":").map { it.toInt() }
    val (h2, m2) = t2.split(":").map { it.toInt() }
    return abs((h1 * 60 + m1) - (h2 * 60 + m2))
}


fun currentTimeRounded(): String {
    val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
    val roundedMinute = if (now.minute < 30) 30 else 0
    val roundedHour = if (now.minute < 30) now.hour else now.hour + 1
    return formatTime(roundedHour, roundedMinute)
}

fun formatTime(hour: Int, minute: Int): String {
    val h = hour.toString().padStart(2, '0')
    val m = minute.toString().padStart(2, '0')
    return "$h:$m"
}