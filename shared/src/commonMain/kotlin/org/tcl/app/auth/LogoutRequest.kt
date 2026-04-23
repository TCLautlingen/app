package org.tcl.app.auth

import kotlinx.serialization.Serializable


@Serializable
data class LogoutRequest(
    val refreshToken: String = "",
)