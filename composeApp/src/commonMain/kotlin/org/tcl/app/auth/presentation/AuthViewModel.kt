package org.tcl.app.auth.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.tcl.app.auth.domain.AuthRepository
import org.tcl.app.auth.domain.toText
import org.tcl.app.core.data.TokenManager
import org.tcl.app.core.domain.util.onFailure
import org.tcl.app.core.domain.util.onSuccess

class AuthViewModel(
    private val repository: AuthRepository,
    private val tokenManager: TokenManager
) : ViewModel() {
    private val _state = MutableStateFlow(AuthState())
    val state = _state.asStateFlow()

    private val _events = Channel<AuthEvent>()
    val events = _events.receiveAsFlow()

    fun onAction(action: AuthAction) {
        when (action) {
            is AuthAction.OnTabChange -> {
                _state.update { it.copy(selectedTab = action.selectedTab) }
            }
            is AuthAction.OnEmailChange -> {
                _state.update { it.copy(email = action.email) }
            }
            is AuthAction.OnPasswordChange -> {
                _state.update { it.copy(password = action.password) }
            }
            is AuthAction.OnConfirmPasswordChange -> {
                _state.update { it.copy(confirmPassword = action.confirmPassword) }
            }
            is AuthAction.OnFirstNameChange -> {
                _state.update { it.copy(firstName = action.firstName) }
            }
            is AuthAction.OnLastNameChange -> {
                _state.update { it.copy(lastName = action.lastName) }
            }
            AuthAction.OnLoginClick -> login()
            AuthAction.OnRegisterClick -> register()
        }
    }

    private fun login() {
        val currentState = _state.value

        viewModelScope.launch {
            val authTokens = repository.login(
                email = currentState.email,
                password = currentState.password
            )

            if (authTokens != null) {
                tokenManager.tokens = authTokens
                _events.send(AuthEvent.LoggedIn)
            }
        }
    }

    private fun register() {
        val currentState = _state.value

        if (currentState.password != currentState.confirmPassword) {
            _state.update {
                it.copy(errorMessage = "Passwörter stimmen nicht überein")
            }
            return
        }

        viewModelScope.launch {
            repository.register(
                email = currentState.email,
                password = currentState.password,
                firstName = currentState.firstName,
                lastName = currentState.lastName
            )
                .onSuccess { authTokens ->
                    tokenManager.tokens = authTokens
                    _state.update {
                        it.copy(errorMessage = null)
                    }
                    _events.send(AuthEvent.Registered)
                }
                .onFailure { error ->
                    _state.update {
                        it.copy(errorMessage = error.toText())
                    }
                }
        }
    }
}