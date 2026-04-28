package org.tcl.app.user.presentation.profile

import androidx.compose.runtime.Stable
import org.tcl.app.user.DetailedUser
import org.tcl.app.user.User

@Stable
data class UserProfileState(
    val user: DetailedUser? = null,
    val isLoading : Boolean = true,
    val firstName: String = "",
    val lastName: String = "",
    val phoneNumber: String? = null,
    val address: String? = null,
    val isSaving: Boolean = false,
)