package org.tcl.app.onboarding.presentation.membership

import androidx.compose.runtime.Stable

@Stable
data class OnboardingMembershipState(
    val isMember: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)
