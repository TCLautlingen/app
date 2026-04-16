package org.tcl.app.auth.presentation

sealed interface AuthAction {
    data class OnTabChange(val selectedTab : Int) : AuthAction
    data class OnEmailChange(val email: String) : AuthAction
    data class OnPasswordChange(val password: String) : AuthAction
    data class OnConfirmPasswordChange(val confirmPassword: String) : AuthAction
    data class OnFirstNameChange(val firstName: String) : AuthAction
    data class OnLastNameChange(val lastName: String) : AuthAction
    data object OnLoginClick : AuthAction
    data object OnRegisterClick : AuthAction
}