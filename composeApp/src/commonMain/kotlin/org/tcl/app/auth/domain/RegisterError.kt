package org.tcl.app.auth.domain

import org.tcl.app.core.domain.util.Error

sealed interface RegisterError : Error {
    data object EmailAlreadyExists : RegisterError
    data object InvalidEmail : RegisterError
    data object PasswordTooWeak : RegisterError
    data object FirstNameEmpty : RegisterError
    data object LastNameEmpty : RegisterError
    data object Unknown : RegisterError
}

fun RegisterError.toText(): String {
    return when (this) {
        RegisterError.EmailAlreadyExists -> "Email ist bereits registriert"
        RegisterError.InvalidEmail -> "Ungültige Email-Adresse"
        RegisterError.PasswordTooWeak -> "Passwort ist zu schwach"
        RegisterError.FirstNameEmpty -> "Vorname darf nicht leer sein"
        RegisterError.LastNameEmpty -> "Nachname darf nicht leer sein"
        RegisterError.Unknown -> "Unbekannter Fehler"
    }
}