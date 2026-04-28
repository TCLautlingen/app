package org.tcl.app.auth.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.tcl.app.auth.domain.AuthRemoteDataSource
import org.tcl.app.auth.domain.toText
import org.tcl.app.core.data.SecureStorage
import org.tcl.app.core.domain.util.onFailure
import org.tcl.app.core.domain.util.onSuccess

class AuthLoginViewModel(
    private val dataSource: AuthRemoteDataSource,
    private val secureStorage: SecureStorage
) : ViewModel() {
    private val _state = MutableStateFlow(AuthLoginState())
    val state = _state.asStateFlow()

    private val _events = Channel<AuthLoginEvent>()
    val events = _events.receiveAsFlow()

    fun onAction(action: AuthLoginAction) {
        when (action) {
            is AuthLoginAction.OnEmailChange -> {
                _state.update { it.copy(email = action.email) }
            }
            is AuthLoginAction.OnPasswordChange -> {
                _state.update { it.copy(password = action.password) }
            }
            AuthLoginAction.OnLoginClick -> login()
        }
    }

    private fun login() {
        val currentState = _state.value

        _state.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            dataSource.login(
                email = currentState.email,
                password = currentState.password
            )
                .onSuccess { authTokens ->
                    secureStorage.tokens = authTokens
                    _state.update {
                        it.copy(errorMessage = null, isLoading = false)
                    }
                    _events.send(AuthLoginEvent.LoggedIn)
                }
                .onFailure { error ->
                    _state.update {
                        it.copy(errorMessage = error.toText(), isLoading = false)
                    }
                }
        }
    }
}