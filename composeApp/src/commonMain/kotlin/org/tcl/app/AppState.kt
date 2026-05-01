package org.tcl.app

data class AppState(
    val isLoading: Boolean = true,
    val isLoggedIn: Boolean = false,
    val currentUserId: Int? = null,
)