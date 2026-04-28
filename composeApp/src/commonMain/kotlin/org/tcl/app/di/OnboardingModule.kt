package org.tcl.app.di

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import org.tcl.app.onboarding.presentation.account.OnboardingAccountViewModel
import org.tcl.app.onboarding.presentation.contact.OnboardingContactViewModel
import org.tcl.app.onboarding.presentation.membership.OnboardingMembershipViewModel
import org.tcl.app.onboarding.presentation.profile.OnboardingProfileViewModel

val onboardingModule = module {
    viewModelOf(::OnboardingAccountViewModel)
    viewModelOf(::OnboardingMembershipViewModel)
    viewModelOf(::OnboardingProfileViewModel)
    viewModelOf(::OnboardingContactViewModel)
}
