package org.tcl.app.user.presentation.profile

import org.tcl.app.user.User

sealed interface UserProfileAction {
    data class OnFirstNameChange(val firstName: String) : UserProfileAction
    data class OnLastNameChange(val lastName: String) : UserProfileAction
    data class OnPhoneNumberChange(val phoneNumber: String) : UserProfileAction
    data class OnAddressChange(val address: String) : UserProfileAction
    data object OnSaveClick : UserProfileAction
    data object OnLogoutClick : UserProfileAction
}