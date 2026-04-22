package org.tcl.app.booking

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.serialization.Serializable
import org.tcl.app.user.User


@Serializable
data class Booking(
    val id: Int,
    val userId: Int,
    val courtId: Int,
    val date: LocalDate,
    val startTime: LocalTime,
    val duration: Int,
    val players: List<User>,
    val isOwner: Boolean? = null,
)


val VALID_BOOKING_DURATIONS = listOf(30, 60, 90, 120)
