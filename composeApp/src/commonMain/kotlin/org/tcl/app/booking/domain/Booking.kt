package org.tcl.app.booking.domain

import kotlinx.datetime.LocalDate

data class Booking(
    val id: String,
    val date: LocalDate,
    val startTime: String,
    val duration: Int,
    val court: Int,
)