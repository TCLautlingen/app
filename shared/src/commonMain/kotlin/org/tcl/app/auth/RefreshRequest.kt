package org.tcl.app.auth

import kotlinx.serialization.Serializable


@Serializable
data class RefreshRequest(
    val refreshToken: String = "",
)
