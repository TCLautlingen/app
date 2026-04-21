package org.tcl.app.booking.presentation.list

import org.tcl.app.booking.Booking
import org.tcl.app.util.formatDdMmYyyy


data class BookingUi(
    val id: String,
    val date: String,
    val startTime: String,
    val duration: String,
    val courtName: String,
    val isOwner: Boolean,
)

fun Booking.toBookingUi(): BookingUi {
    return BookingUi(
        id = id.toString(),
        date = date.formatDdMmYyyy(),
        startTime = startTime.toString(),
        duration = duration.toString(),
        courtName = "Platz $courtId",
        isOwner = isOwner ?: false,
    )
}