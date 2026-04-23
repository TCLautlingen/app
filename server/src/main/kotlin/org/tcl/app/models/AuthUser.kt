package org.tcl.app.models

data class AuthUser(
    val id: Int,
    val email: String,
    val passwordHash: String,
    val passwordSalt: String,
)
