package org.tcl.app

import kotlinx.serialization.Serializable

@Serializable
data class AuthTokens(
    val accessToken: String = "",
    val refreshToken: String = "",
)

@Serializable
data class RegisterRequest(
    val email: String = "",
    val password: String = "",
)

@Serializable
data class LoginRequest(
    val email: String = "",
    val password: String = "",
)