package org.tcl.app.booking

import kotlinx.serialization.Serializable


@Serializable
data class BookingRequest(
    val courtId: Int,
    val date: String,
    val startTime: String,
    val duration: Int,
    val playerIds: List<Int>,
)