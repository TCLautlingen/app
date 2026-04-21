package org.tcl.app.auth

import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequest(
    val email: String = "",
    val password: String = "",
    val firstName: String = "",
    val lastName: String = "",
)