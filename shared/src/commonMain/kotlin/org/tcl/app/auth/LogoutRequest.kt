package org.tcl.app.auth

import kotlinx.serialization.Serializable


@Serializable
data class LogoutRequest(
    val deviceUniqueId: String = "",
    val refreshToken: String = "",
)