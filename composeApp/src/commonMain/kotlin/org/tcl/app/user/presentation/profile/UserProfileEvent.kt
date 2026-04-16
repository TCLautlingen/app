package org.tcl.app.user.presentation.profile

sealed interface UserProfileEvent {
    data object LoggedOut : UserProfileEvent
}