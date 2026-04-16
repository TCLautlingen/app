package org.tcl.app.auth.presentation

sealed interface AuthEvent {
    data object LoggedIn : AuthEvent
    data object Registered : AuthEvent
}