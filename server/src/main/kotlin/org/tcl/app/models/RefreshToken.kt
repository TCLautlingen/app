package org.tcl.app.models

data class RefreshToken(
    val token: String,
    val userId: Int,
    val expiresAt: Long
)
