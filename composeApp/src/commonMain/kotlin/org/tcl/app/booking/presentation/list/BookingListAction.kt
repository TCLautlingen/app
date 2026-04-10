package org.tcl.app.booking.presentation.list

sealed interface BookingListAction {
    data object OnDeleteClick : BookingListAction
    data object OnConfirmDelete : BookingListAction
    data object OnDismissDeleteDialog : BookingListAction
}
