package org.tcl.app.booking

import kotlinx.serialization.Serializable

@Serializable
data class CourtSlot(
    val startTime: String,
    val taken: Boolean
)
