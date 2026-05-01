package org.tcl.app.booking

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed interface BookingWsMessage {
    @Serializable
    @SerialName("booking_created")
    data class BookingCreated(val booking: Booking) : BookingWsMessage

    @Serializable
    @SerialName("booking_deleted")
    data class BookingDeleted(val bookingId: Int) : BookingWsMessage
}
