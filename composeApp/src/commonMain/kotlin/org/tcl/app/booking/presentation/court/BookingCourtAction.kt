package org.tcl.app.booking.presentation.court

sealed interface BookingCourtAction {
    data object OnRefresh : BookingCourtAction
    data class OnCourtChange(val court: Int) : BookingCourtAction
}