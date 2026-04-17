package org.tcl.app.booking.presentation.list

import org.tcl.app.Booking


data class BookingUi(
    val id: String,
    val date: String,
    val startTime: String,
    val duration: String,
    val court: String,
)

fun Booking.toBookingUi(): BookingUi {
    return BookingUi(
        id = id.toString(),
        date = date.toString(),
        startTime = startTime.toString(),
        duration = duration.toString(),
        court = courtId.toString(),
    )
}