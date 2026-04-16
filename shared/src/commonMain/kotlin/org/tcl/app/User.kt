package org.tcl.app

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val isAdmin: Boolean,
    val isMember: Boolean,
)

@Serializable
data class RefreshToken(
    val token: String,
    val userId: String,
    val expiresAt: Long
)