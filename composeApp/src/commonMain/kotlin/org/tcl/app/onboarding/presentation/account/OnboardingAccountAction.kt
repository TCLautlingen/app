package org.tcl.app.onboarding.presentation.account

sealed interface OnboardingAccountAction {
    data class OnEmailChange(val email: String) : OnboardingAccountAction
    data class OnPasswordChange(val password: String) : OnboardingAccountAction
    data class OnConfirmPasswordChange(val confirmPassword: String) : OnboardingAccountAction
    data class OnTermsCheck(val checked: Boolean) : OnboardingAccountAction
    data object OnNextClick : OnboardingAccountAction
}
