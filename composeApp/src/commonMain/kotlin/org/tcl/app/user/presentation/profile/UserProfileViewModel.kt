package org.tcl.app.user.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ktor.client.plugins.auth.clearAuthTokens
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.tcl.app.core.data.ApiClient
import org.tcl.app.core.data.TokenManager
import org.tcl.app.core.domain.util.onFailure
import org.tcl.app.core.domain.util.onSuccess
import org.tcl.app.user.domain.UserRepository

class UserProfileViewModel(
    private val tokenManager: TokenManager,
    private val apiClient: ApiClient,
    private val repository: UserRepository
) : ViewModel() {
    private val _state = MutableStateFlow(UserProfileState())
    val state = _state.asStateFlow()

    private val _events = Channel<UserProfileEvent>()
    val events = _events.receiveAsFlow()

    init {
        loadUserProfile()
    }

    fun onAction(action: UserProfileAction) {
        when (action) {
            is UserProfileAction.OnLogoutClick -> logout()
        }
    }

    private fun loadUserProfile() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            repository.getCurrentUser()
                .onSuccess { user ->
                    _state.update {
                        it.copy(
                            user = user,
                            isLoading = false
                        )
                    }
                }
                .onFailure {
                    _state.update { it.copy(isLoading = false) }
                }
        }
    }

    private fun logout() {
        viewModelScope.launch {
            tokenManager.clear()
            apiClient.client.clearAuthTokens()
            _events.send(UserProfileEvent.LoggedOut)
        }
    }
}