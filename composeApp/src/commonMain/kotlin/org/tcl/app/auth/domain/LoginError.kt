package org.tcl.app.auth.domain

import org.tcl.app.core.domain.util.Error

sealed interface LoginError : Error {
    data object InvalidCredentials : LoginError
    data object Unknown : LoginError
}

fun LoginError.toText(): String {
    return when (this) {
        LoginError.InvalidCredentials -> "Ungültige Anmeldedaten"
        LoginError.Unknown -> "Unbekannter Fehler"
    }
}