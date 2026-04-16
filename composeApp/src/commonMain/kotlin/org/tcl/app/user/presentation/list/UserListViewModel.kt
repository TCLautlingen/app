package org.tcl.app.user.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.tcl.app.user.domain.UserRepository

class UserListViewModel(
    private val repository: UserRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(UserListState())
    val state = _state.asStateFlow()

    init {
        updateUsers()
    }

    fun onAction(action: UserListAction) {
        when (action) {
            is UserListAction.OnSearchQueryChange -> {
                _state.update { it.copy(searchQuery = action.searchQuery) }
                updateUsers()
            }
        }
    }

    private fun updateUsers() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val users = repository.getUsers(_state.value.searchQuery)
            _state.update {
                it.copy(
                    users = users,
                    isLoading = false
                )
            }
        }
    }
}