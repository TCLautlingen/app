package org.tcl.app.user

data class AuthUser(
    val id: Int,
    val email: String,
    val passwordHash: String,
    val passwordSalt: String,
    val firstName: String,
    val lastName: String
)