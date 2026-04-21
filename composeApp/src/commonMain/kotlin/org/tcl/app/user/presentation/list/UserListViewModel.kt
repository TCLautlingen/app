package org.tcl.app.user.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.tcl.app.core.domain.util.onFailure
import org.tcl.app.core.domain.util.onSuccess
import org.tcl.app.user.domain.UserRemoteDataSource

class UserListViewModel(
    private val dataSource: UserRemoteDataSource,
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
            dataSource.getUsers(_state.value.searchQuery)
                .onSuccess { users ->
                    _state.update {
                        it.copy(
                            users = users,
                            isLoading = false
                        )
                    }
                }
                .onFailure {
                    _state.update { it.copy(isLoading = false) }
                }
        }
    }
}