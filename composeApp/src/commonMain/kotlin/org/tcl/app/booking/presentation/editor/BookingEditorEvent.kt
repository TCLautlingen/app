package org.tcl.app.booking.presentation.editor

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime

sealed interface BookingEditorEvent {
    data class CourtBooked(
        val date: LocalDate,
        val startTime: LocalTime,
        val durationMinutes: Int,
        val courtName: String,
    ) : BookingEditorEvent
}