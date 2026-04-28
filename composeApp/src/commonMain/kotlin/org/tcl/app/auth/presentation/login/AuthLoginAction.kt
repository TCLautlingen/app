package org.tcl.app.auth.presentation.login

sealed interface AuthLoginAction {
    data class OnEmailChange(val email: String) : AuthLoginAction
    data class OnPasswordChange(val password: String) : AuthLoginAction
    data object OnLoginClick : AuthLoginAction
}