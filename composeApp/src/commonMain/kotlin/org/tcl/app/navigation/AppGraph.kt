package org.tcl.app.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable
import org.tcl.app.User

@Serializable
sealed interface AppGraph : NavKey {

    @Serializable
    data object Auth : AppGraph

    @Serializable
    data object BookingList : AppGraph

    @Serializable
    data class CreateBooking(
        val date: String? = null,
        val court: Int? = null,
        val startTime: String? = null,
    ) : AppGraph

    @Serializable
    data object BookingCourt : AppGraph

    @Serializable
    data object UserProfile : AppGraph

    @Serializable
    data object UserList : AppGraph

    @Serializable
    data class UserEditor(
        val user: User
    ) : AppGraph
}