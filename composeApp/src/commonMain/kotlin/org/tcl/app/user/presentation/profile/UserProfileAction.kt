package org.tcl.app.user.presentation.profile

sealed interface UserProfileAction {
    data object OnLogoutClick : UserProfileAction
}