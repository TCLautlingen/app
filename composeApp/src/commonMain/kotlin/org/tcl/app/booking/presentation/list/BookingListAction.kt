package org.tcl.app.booking.presentation.list

sealed interface BookingListAction {
    data class OnDeleteClick(val bookingId: String) : BookingListAction
    data object OnConfirmDelete : BookingListAction
    data object OnDismissDeleteDialog : BookingListAction
    data object OnRefresh : BookingListAction
}
