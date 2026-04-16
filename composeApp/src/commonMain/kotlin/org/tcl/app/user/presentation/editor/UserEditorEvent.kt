package org.tcl.app.user.presentation.editor

sealed interface UserEditorEvent {
    data object UserSaved : UserEditorEvent
}