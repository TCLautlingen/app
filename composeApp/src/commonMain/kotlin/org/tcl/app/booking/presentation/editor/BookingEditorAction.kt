package org.tcl.app.booking.presentation.editor

import kotlinx.datetime.LocalDate

sealed interface BookingEditorAction {
    data class OnDateChange(val date: LocalDate) : BookingEditorAction
    data class OnDurationChange(val duration: Int) : BookingEditorAction
    data class OnStartTimeChange(val startTime: String) : BookingEditorAction
    data class OnCourtChange(val court: Int) : BookingEditorAction
    data object OnBookClick : BookingEditorAction
}