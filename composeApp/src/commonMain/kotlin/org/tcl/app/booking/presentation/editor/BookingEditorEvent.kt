package org.tcl.app.booking.presentation.editor

import org.tcl.app.booking.Booking

sealed interface BookingEditorEvent {
    data class CourtBooked(
        val booking: Booking,
    ) : BookingEditorEvent
}