package org.tcl.app.user.presentation.list

sealed interface UserListAction {
    data class OnSearchQueryChange(val searchQuery: String) : UserListAction
}