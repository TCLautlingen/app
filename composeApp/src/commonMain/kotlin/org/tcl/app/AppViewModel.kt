package org.tcl.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mmk.kmpnotifier.notification.NotifierManager
import io.ktor.client.plugins.auth.clearAuthTokens
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.tcl.app.auth.domain.AuthRemoteDataSource
import org.tcl.app.core.data.SecureStorage
import org.tcl.app.core.data.network.BackendApiClient
import org.tcl.app.core.domain.util.onFailure
import org.tcl.app.core.domain.util.onSuccess
import org.tcl.app.notification.domain.NotificationRemoteDataSource

class AppViewModel(
    private val secureStorage: SecureStorage,
    private val backendApiClient: BackendApiClient,
    private val authRemoteDataSource: AuthRemoteDataSource,
    private val notificationRemoteDataSource: NotificationRemoteDataSource,
) : ViewModel() {
    private val _state = MutableStateFlow(AppState())
    val state = _state.asStateFlow()

    init {
        setupTokenListener()
        checkAuth()
    }

    private fun setupTokenListener() {
        NotifierManager.addListener(object : NotifierManager.Listener {
            override fun onNewToken(token: String) {
                if (_state.value.isLoggedIn) {
                    viewModelScope.launch {
                        notificationRemoteDataSource.registerToken(token)
                    }
                }
            }
        })
    }

    private fun checkAuth() {
        viewModelScope.launch {
            val refreshToken = secureStorage.tokens.refreshToken
            if (refreshToken.isBlank()) {
                _state.update { it.copy(isLoading = false) }
                return@launch
            }
            authRemoteDataSource.refresh(refreshToken)
                .onSuccess { authTokens ->
                    secureStorage.tokens = authTokens
                    _state.update { it.copy(isLoggedIn = true, isLoading = false) }
                }
                .onFailure {
                    _state.update { it.copy(isLoading = false) }
                }
        }
    }

    fun setLoggedIn() {
        _state.update { it.copy(isLoggedIn = true) }
        viewModelScope.launch {
            val token = NotifierManager.getPushNotifier().getToken()
            if (token != null) {
                notificationRemoteDataSource.registerToken(token)
            }
        }
    }

    fun setLoggedOut() {
        viewModelScope.launch {
            val token = NotifierManager.getPushNotifier().getToken()
            if (token != null) {
                notificationRemoteDataSource.unregisterToken(token)
            }
            authRemoteDataSource.logout(secureStorage.tokens.refreshToken)
            secureStorage.clearAuthTokens()
            backendApiClient.client.clearAuthTokens()
            _state.update { it.copy(isLoggedIn = false) }
        }
    }
}
