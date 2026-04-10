package org.tcl.app.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface AppGraph : NavKey {

    @Serializable
    data object Login : AppGraph

    @Serializable
    data object BookingsList : AppGraph

    @Serializable
    data object CreateBooking : AppGraph
}