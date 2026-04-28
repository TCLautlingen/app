package org.tcl.app.auth.presentation.login

import androidx.compose.runtime.Stable

@Stable
data class AuthLoginState(
    val email: String = "",
    val password: String = "",
    val errorMessage: String? = null,
    val isLoading: Boolean = false,
)