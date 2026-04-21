package org.tcl.app.booking

import kotlinx.serialization.Serializable
import org.tcl.app.court.Court

@Serializable
data class AvailableSlot(
    val startTime: String,
    val court: Court
)