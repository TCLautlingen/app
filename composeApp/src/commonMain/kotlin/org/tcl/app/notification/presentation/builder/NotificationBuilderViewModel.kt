package org.tcl.app.notification.presentation.builder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.tcl.app.core.domain.util.onFailure
import org.tcl.app.core.domain.util.onSuccess
import org.tcl.app.notification.domain.NotificationRemoteDataSource

class NotificationBuilderViewModel(
    private val dataSource: NotificationRemoteDataSource
) : ViewModel() {
    private val _state = MutableStateFlow(NotificationBuilderState())
    val state = _state.asStateFlow()

    fun onAction(action: NotificationBuilderAction) {
        when (action) {
            is NotificationBuilderAction.OnTitleChange -> {
                _state.update { it.copy(title = action.title) }
            }
            is NotificationBuilderAction.OnBodyChange -> {
                _state.update { it.copy(body = action.body) }
            }
            is NotificationBuilderAction.OnSendClick -> {
                viewModelScope.launch {
                    dataSource.sendNotification(
                        title = state.value.title,
                        body = state.value.body
                    )
                        .onSuccess {

                        }
                        .onFailure {

                        }
                }
            }
        }
    }
}