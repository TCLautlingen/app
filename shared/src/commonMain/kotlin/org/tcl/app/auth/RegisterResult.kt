package org.tcl.app.auth

sealed class RegisterResult {
    data class Success(val tokens: AuthTokens) : RegisterResult()
    data object EmailAlreadyExists : RegisterResult()
}

const val EMAIL_ALREADY_EXISTS_ERROR = "Email already exists"
const val VALIDATION_ERROR_EMAIL = "Invalid email format"
const val VALIDATION_ERROR_PASSWORD = "Password too short"