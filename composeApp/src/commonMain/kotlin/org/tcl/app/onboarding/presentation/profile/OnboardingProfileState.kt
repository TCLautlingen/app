package org.tcl.app.onboarding.presentation.profile

import androidx.compose.runtime.Stable

@Stable
data class OnboardingProfileState(
    val firstName: String = "",
    val lastName: String = "",
    val firstNameError: String? = null,
    val lastNameError: String? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)
