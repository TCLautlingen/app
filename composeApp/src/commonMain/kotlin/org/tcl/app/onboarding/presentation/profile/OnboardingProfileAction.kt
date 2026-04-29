package org.tcl.app.onboarding.presentation.profile

sealed interface OnboardingProfileAction {
    data class OnFirstNameChange(val firstName: String) : OnboardingProfileAction
    data class OnLastNameChange(val lastName: String) : OnboardingProfileAction
    data class OnPhoneNumberChange(val phoneNumber: String) : OnboardingProfileAction
    data class OnAddressChange(val address: String) : OnboardingProfileAction
    data object OnNextClick : OnboardingProfileAction
}
