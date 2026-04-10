package org.tcl.app.booking.presentation.list

import androidx.compose.runtime.Stable

@Stable
data class BookingListState(
    val bookings: List<BookingUi> = emptyList(),
    val isLoading : Boolean = true,
    val showDeleteDialog: Boolean = false,
)