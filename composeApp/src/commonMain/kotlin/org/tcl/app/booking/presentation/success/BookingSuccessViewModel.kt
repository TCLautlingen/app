package org.tcl.app.booking.presentation.success

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import org.tcl.app.core.domain.CalendarService

class BookingSuccessViewModel : ViewModel() {
    private val _state = MutableStateFlow(BookingSuccessState())
    val state = _state.asStateFlow()

    private val _calendarService = CalendarService()

    fun initialize(
        date: LocalDate,
        startTime: LocalTime,
        durationMinutes: Int,
        courtName: String,
    ) {
        _state.value = BookingSuccessState(
            date = date,
            startTime = startTime,
            durationMinutes = durationMinutes,
            courtName = courtName,
        )
    }

    fun onAction(action: BookingSuccessAction) {
        when (action) {
            BookingSuccessAction.OnAddToCalendarClick -> addToCalendar()
        }
    }

    private fun addToCalendar() {
        val currentState = _state.value

        val startInstant = LocalDateTime(currentState.date, currentState.startTime)
            .toInstant(TimeZone.currentSystemDefault())
        val startMillis = startInstant.toEpochMilliseconds()
        val endMillis = startMillis + currentState.durationMinutes * 60 * 1000L

        _calendarService.openCalendarWithEvent(
            title = "Tennis",
            description = currentState.courtName,
            location = "Tennisplatz Lautlingen",
            startTimeMillis = startMillis,
            endTimeMillis = endMillis,
        )
    }
}