package org.tcl.app

import kotlinx.serialization.Serializable

@Serializable
data class Booking(
    val id: String = "",
    val userId: String = "",
    val date: String,
    val startTime: String,
    val duration: Int,
    val court: Int,
)

@Serializable
data class CourtSlot(
    val startTime: String,
    val taken: Boolean
)

@Serializable
data class AvailableSlot(
    val startTime: String,
    val court: Int
)