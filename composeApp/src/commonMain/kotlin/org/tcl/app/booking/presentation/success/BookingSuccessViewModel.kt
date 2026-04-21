package org.tcl.app.booking.presentation.success

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import org.tcl.app.booking.Booking
import org.tcl.app.core.domain.CalendarService

class BookingSuccessViewModel : ViewModel() {
    private val _state = MutableStateFlow(BookingSuccessState())
    val state = _state.asStateFlow()

    private val _calendarService = CalendarService()

    fun initialize(
        booking: Booking,
    ) {
        _state.value = BookingSuccessState(
            booking = booking,
        )
    }

    fun onAction(action: BookingSuccessAction) {
        when (action) {
            BookingSuccessAction.OnAddToCalendarClick -> addToCalendar()
        }
    }

    private fun addToCalendar() {
        val currentState = _state.value

        if (currentState.booking == null) {
            return
        }

        val startInstant = LocalDateTime(currentState.booking.date, currentState.booking.startTime)
            .toInstant(TimeZone.currentSystemDefault())
        val startMillis = startInstant.toEpochMilliseconds()
        val endMillis = startMillis + currentState.booking.duration * 60 * 1000L

        _calendarService.openCalendarWithEvent(
            title = "Tennis",
            description = "Platz " + currentState.booking.courtId.toString(),
            location = "Tennisplatz Lautlingen",
            startTimeMillis = startMillis,
            endTimeMillis = endMillis,
        )
    }
}