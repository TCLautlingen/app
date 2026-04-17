package org.tcl.app.booking.presentation.editor

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime

sealed interface BookingEditorAction {
    data object OnDateClick : BookingEditorAction
    data class OnDateChange(val date: LocalDate) : BookingEditorAction
    data object OnDateChangeDismiss : BookingEditorAction
    data class OnDurationChange(val duration: Int) : BookingEditorAction
    data class OnStartTimeChange(val startTime: LocalTime) : BookingEditorAction
    data class OnCourtChange(val courtId: Int) : BookingEditorAction
    data object OnBookClick : BookingEditorAction
}