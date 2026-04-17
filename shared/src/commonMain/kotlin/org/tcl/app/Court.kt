package org.tcl.app

import kotlinx.serialization.Serializable

@Serializable
data class Court(
    val id: Int = 0,
    val name: String = "",
)