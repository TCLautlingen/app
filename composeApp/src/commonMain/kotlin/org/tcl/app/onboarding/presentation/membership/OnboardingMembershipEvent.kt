package org.tcl.app.onboarding.presentation.membership

sealed interface OnboardingMembershipEvent {
    data object SavedSuccessfully : OnboardingMembershipEvent
}
