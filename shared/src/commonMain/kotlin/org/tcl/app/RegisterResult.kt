package org.tcl.app

sealed class RegisterResult {
    data class Success(val tokens: AuthTokens) : RegisterResult()
    data object EmailAlreadyExists : RegisterResult()
    data class ValidationError(val message: String) : RegisterResult()
}

const val EMAIL_ALREADY_EXISTS_ERROR = "Email already exists"
const val VALIDATION_ERROR_EMAIL = "Invalid email format"
const val VALIDATION_ERROR_PASSWORD = "Password too short"
const val VALIDATION_ERROR_FIRST_NAME = "First name cannot be empty"
const val VALIDATION_ERROR_LAST_NAME = "Last name cannot be empty"