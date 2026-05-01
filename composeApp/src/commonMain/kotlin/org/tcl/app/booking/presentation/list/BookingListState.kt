package org.tcl.app.booking.presentation.list

import androidx.compose.runtime.Stable
import org.tcl.app.booking.Booking

@Stable
data class BookingListState(
    val bookings: List<Booking> = emptyList(),
    val isLoading: Boolean = true,
    val isOffline: Boolean = false,
    val showDeleteDialog: Boolean = false,
    val bookingIdToDelete: Int? = null,
)
