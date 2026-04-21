package org.tcl.app.user.presentation.list

import androidx.compose.runtime.Stable
import org.tcl.app.user.User

@Stable
data class UserListState(
    val users: List<User> = emptyList(),
    val searchQuery: String = "",
    val isLoading: Boolean = true,
)