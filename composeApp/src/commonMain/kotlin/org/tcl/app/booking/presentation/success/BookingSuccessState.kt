package org.tcl.app.booking.presentation.success

import androidx.compose.runtime.Stable
import org.tcl.app.booking.Booking

@Stable
data class BookingSuccessState(
    val booking: Booking? = null,
)