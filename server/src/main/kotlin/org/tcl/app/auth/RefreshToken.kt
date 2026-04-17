package org.tcl.app.auth

data class RefreshToken(
    val token: String,
    val userId: Int,
    val expiresAt: Long
)