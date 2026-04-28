package org.tcl.app.user.presentation.editor

import androidx.compose.runtime.Stable
import org.tcl.app.user.DetailedUser

@Stable
data class UserEditorState(
    val userId: Int,
    val user: DetailedUser? = null,
)