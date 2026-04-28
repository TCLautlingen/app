package org.tcl.app.auth

import kotlinx.serialization.Serializable

sealed class RegisterResult {
    data class Success(val tokens: AuthTokens) : RegisterResult()
    data class Errors(val errors: List<RegisterError>) : RegisterResult()
}

enum class RegisterField {
    EMAIL,
    PASSWORD,
}

enum class RegisterErrorCode {
    EMAIL_INVALID,
    EMAIL_TOO_LONG,
    EMAIL_ALREADY_EXISTS,
    PASSWORD_TOO_SHORT,
    PASSWORD_TOO_LONG,
    PASSWORD_MISSING_DIGIT,
}

@Serializable
data class RegisterError(
    val field: RegisterField,
    val code: RegisterErrorCode,
    val params: Map<String, String> = emptyMap()
)