package org.tcl.app.user.presentation.editor

sealed interface UserEditorAction {
    data object OnSaveClick : UserEditorAction
}