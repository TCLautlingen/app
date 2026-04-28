package org.tcl.app.onboarding.presentation.contact

sealed interface OnboardingContactEvent {
    data object SavedSuccessfully : OnboardingContactEvent
}
