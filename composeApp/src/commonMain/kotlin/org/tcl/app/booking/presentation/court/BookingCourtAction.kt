package org.tcl.app.booking.presentation.court

import kotlinx.datetime.LocalDate

sealed interface BookingCourtAction {
    data object OnRefresh : BookingCourtAction
    data object OnDateClick : BookingCourtAction
    data class OnDateChange(val date: LocalDate) : BookingCourtAction
    data object OnDateChangeDismiss : BookingCourtAction
    data class OnCourtChange(val court: Int) : BookingCourtAction
}