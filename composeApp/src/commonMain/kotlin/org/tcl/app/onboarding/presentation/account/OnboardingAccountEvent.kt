package org.tcl.app.onboarding.presentation.account

sealed interface OnboardingAccountEvent {
    data object RegisteredSuccessfully : OnboardingAccountEvent
}
