package org.tcl.app.auth.presentation

import androidx.compose.runtime.Stable

@Stable
data class AuthState(
    val selectedTab: Int = 0,
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val errorMessage: String? = null,
)