package org.tcl.app.onboarding.presentation.profile

sealed interface OnboardingProfileEvent {
    data object SavedSuccessfully : OnboardingProfileEvent
}
