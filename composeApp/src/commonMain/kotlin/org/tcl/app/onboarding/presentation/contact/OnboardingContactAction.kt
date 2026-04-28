package org.tcl.app.onboarding.presentation.contact

sealed interface OnboardingContactAction {
    data class OnPhoneNumberChange(val phoneNumber: String) : OnboardingContactAction
    data class OnAddressChange(val address: String) : OnboardingContactAction
    data object OnNextClick : OnboardingContactAction
}
