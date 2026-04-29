package org.tcl.app.user.presentation.editor

sealed interface UserEditorAction {
    data object OnSaveClick : UserEditorAction
    data class OnMemberToggle(val isMember: Boolean) : UserEditorAction
    data class OnAdminToggle(val isAdmin: Boolean) : UserEditorAction
}