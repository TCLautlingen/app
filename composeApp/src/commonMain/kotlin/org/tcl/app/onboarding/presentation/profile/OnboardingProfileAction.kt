package org.tcl.app.onboarding.presentation.profile

sealed interface OnboardingProfileAction {
    data class OnFirstNameChange(val firstName: String) : OnboardingProfileAction
    data class OnLastNameChange(val lastName: String) : OnboardingProfileAction
    data object OnNextClick : OnboardingProfileAction
}
