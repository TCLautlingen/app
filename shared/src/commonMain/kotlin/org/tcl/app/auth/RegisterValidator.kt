package org.tcl.app.auth

object RegisterValidator {
    const val PASSWORD_MIN = 8
    const val PASSWORD_MAX = 128
    const val EMAIL_MAX = 254

    private val EMAIL_REGEX = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")

    fun validateEmail(email: String): RegisterError? = when {
        email.isBlank() -> RegisterError(RegisterField.EMAIL, RegisterErrorCode.EMAIL_INVALID)
        email.length > EMAIL_MAX -> RegisterError(RegisterField.EMAIL, RegisterErrorCode.EMAIL_TOO_LONG)
        !email.matches(EMAIL_REGEX) -> RegisterError(RegisterField.EMAIL, RegisterErrorCode.EMAIL_INVALID)
        else -> null
    }

    fun validatePassword(password: String): RegisterError? = when {
        password.length < PASSWORD_MIN -> RegisterError(
            RegisterField.PASSWORD,
            RegisterErrorCode.PASSWORD_TOO_SHORT,
            mapOf("min" to PASSWORD_MIN.toString())
        )
        password.length > PASSWORD_MAX -> RegisterError(RegisterField.PASSWORD, RegisterErrorCode.PASSWORD_TOO_LONG)
        !password.any { it.isDigit() } -> RegisterError(RegisterField.PASSWORD, RegisterErrorCode.PASSWORD_MISSING_DIGIT)
        else -> null
    }

    fun validate(email: String, password: String): List<RegisterError> = buildList {
        validateEmail(email)?.let { add(it) }
        validatePassword(password)?.let { add(it) }
    }
}