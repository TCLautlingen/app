package org.tcl.app.notification.presentation.inbox

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.tcl.app.core.domain.util.onFailure
import org.tcl.app.core.domain.util.onSuccess
import org.tcl.app.notification.domain.NotificationRemoteDataSource

class NotificationInboxViewModel(
    private val dataSource: NotificationRemoteDataSource
) : ViewModel() {
    private val _state = MutableStateFlow(NotificationInboxState())
    val state = _state.asStateFlow()

    init {
        loadInbox()
    }

    fun onAction(action: NotificationInboxAction) {
        when (action) {
            NotificationInboxAction.OnRefresh -> loadInbox()
        }
    }

    private fun loadInbox() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            dataSource.getInbox()
                .onSuccess { items ->
                    _state.update { it.copy(notifications = items, isLoading = false) }
                }
                .onFailure {
                    _state.update { it.copy(isLoading = false) }
                }
        }
    }
}
