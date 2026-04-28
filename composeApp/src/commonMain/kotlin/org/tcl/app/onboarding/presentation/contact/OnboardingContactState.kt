package org.tcl.app.onboarding.presentation.contact

import androidx.compose.runtime.Stable

@Stable
data class OnboardingContactState(
    val phoneNumber: String = "",
    val address: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)
