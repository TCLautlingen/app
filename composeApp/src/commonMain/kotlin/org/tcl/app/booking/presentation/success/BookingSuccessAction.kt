package org.tcl.app.booking.presentation.success

sealed interface BookingSuccessAction {
    data object OnAddToCalendarClick : BookingSuccessAction
}