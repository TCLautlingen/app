package org.tcl.app.onboarding.presentation.account

import androidx.compose.runtime.Stable

@Stable
data class OnboardingAccountState(
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val termsChecked: Boolean = false,
    val emailError: String? = null,
    val passwordError: String? = null,
    val confirmPasswordError: String? = null,
    val termsError: String? = null,
    val isLoading: Boolean = false,
)
