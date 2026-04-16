package org.tcl.app.user.presentation.profile

import androidx.compose.runtime.Stable
import org.tcl.app.User

@Stable
data class UserProfileState(
    val user: User? = null,
    val isLoading : Boolean = true,
)