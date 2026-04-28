package org.tcl.app.auth.presentation.login

sealed interface AuthLoginEvent {
    data object LoggedIn : AuthLoginEvent
}