package org.tcl.app.user.presentation.editor

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.tcl.app.core.domain.util.onFailure
import org.tcl.app.core.domain.util.onSuccess
import org.tcl.app.user.domain.UserRemoteDataSource

class UserEditorViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val userRemoteDataSource: UserRemoteDataSource
) : ViewModel() {
    private val _state = MutableStateFlow(UserEditorState(
        userId = savedStateHandle["userId"] ?: 0
    ))
    val state = _state.asStateFlow()

    private val _events = Channel<UserEditorEvent>()
    val events = _events.receiveAsFlow()

    fun initialize(userId: Int) {
        _state.update { it.copy(userId = userId) }
        savedStateHandle["userId"] = userId
        loadUser(userId)
    }

    fun onAction(action: UserEditorAction) {
        when (action) {
            is UserEditorAction.OnSaveClick -> {
                viewModelScope.launch {
                    _events.send(UserEditorEvent.UserSaved)
                }
            }
        }
    }

    fun loadUser(userId: Int) {
        viewModelScope.launch {
            userRemoteDataSource.getUserById(userId)
                .onSuccess { user ->
                    _state.update { it.copy(user = user) }
                }
                .onFailure {

                }
        }
    }
}