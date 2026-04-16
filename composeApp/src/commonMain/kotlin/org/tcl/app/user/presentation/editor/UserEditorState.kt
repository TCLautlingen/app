package org.tcl.app.user.presentation.editor

import androidx.compose.runtime.Stable
import org.tcl.app.User

@Stable
data class UserEditorState(
    val user: User
)