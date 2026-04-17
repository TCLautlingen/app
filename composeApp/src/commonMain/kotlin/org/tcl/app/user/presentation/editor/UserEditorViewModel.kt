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
import org.tcl.app.user.domain.UserRepository

class UserEditorViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val userRepository: UserRepository
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
            val user = userRepository.getUserById(userId)
            _state.update { it.copy(user = user) }
        }
    }
}