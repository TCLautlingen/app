package org.tcl.app.booking.presentation.editor

sealed interface BookingEditorEvent {
    data object CourtBooked : BookingEditorEvent
}