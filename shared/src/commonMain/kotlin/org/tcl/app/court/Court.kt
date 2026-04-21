package org.tcl.app.court

import kotlinx.serialization.Serializable

@Serializable
data class Court(
    val id: Int = 0,
    val name: String = "",
)