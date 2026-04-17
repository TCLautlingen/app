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
import org.tcl.app.User

class UserEditorViewModel(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _state = MutableStateFlow(UserEditorState(
        user = savedStateHandle["user"] ?: User(0, "", "", "", isAdmin = false, isMember = true)
    ))
    val state = _state.asStateFlow()

    private val _events = Channel<UserEditorEvent>()
    val events = _events.receiveAsFlow()

    fun initialize(user: User) {
        _state.update { it.copy(user = user) }
        savedStateHandle["user"] = user
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
}