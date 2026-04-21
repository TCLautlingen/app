package org.tcl.app.auth

import kotlinx.serialization.Serializable

@Serializable
data class AuthTokens(
    val accessToken: String = "",
    val refreshToken: String = "",
)