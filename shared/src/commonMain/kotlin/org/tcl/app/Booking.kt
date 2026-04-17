package org.tcl.app

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.serialization.Serializable

@Serializable
data class BookingRequest(
    val courtId: Int,
    val date: String,
    val startTime: String,
    val duration: Int,
)

@Serializable
data class Booking(
    val id: Int,
    val userId: Int,
    val courtId: Int,
    val date: LocalDate,
    val startTime: LocalTime,
    val duration: Int,
)

@Serializable
data class CourtSlot(
    val startTime: String,
    val taken: Boolean
)

@Serializable
data class AvailableSlot(
    val startTime: String,
    val court: Court
)