package org.tcl.app.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.serialization.Serializable
import org.tcl.app.booking.Booking

@Serializable
sealed interface AppGraph : NavKey {

    @Serializable
    data object Auth : AppGraph

    @Serializable
    data object BookingList : AppGraph

    @Serializable
    data class CreateBooking(
        val date: LocalDate? = null,
        val courtId: Int? = null,
        val startTime: LocalTime? = null,
    ) : AppGraph

    @Serializable
    data class BookingSuccess(
        val booking: Booking
    ) : AppGraph

    @Serializable
    data object BookingCourt : AppGraph

    @Serializable
    data object UserProfile : AppGraph

    @Serializable
    data object UserList : AppGraph

    @Serializable
    data class UserEditor(
        val userId: Int
    ) : AppGraph

    @Serializable
    data object NotificationInbox : AppGraph

    @Serializable
    data object NotificationBuilder : AppGraph
}