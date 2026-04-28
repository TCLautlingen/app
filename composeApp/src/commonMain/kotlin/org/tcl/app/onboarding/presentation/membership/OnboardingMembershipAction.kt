package org.tcl.app.onboarding.presentation.membership

sealed interface OnboardingMembershipAction {
    data class OnMemberToggle(val isMember: Boolean) : OnboardingMembershipAction
    data object OnNextClick : OnboardingMembershipAction
}
