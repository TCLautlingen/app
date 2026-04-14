package org.tcl.app

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: String,
    val email: String,
)

@Serializable
data class RefreshToken(
    val token: String,
    val userId: String,
    val expiresAt: Long
)