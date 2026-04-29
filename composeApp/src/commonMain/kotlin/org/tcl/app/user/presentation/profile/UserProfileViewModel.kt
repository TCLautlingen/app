package org.tcl.app.user.presentation.profile

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

class UserProfileViewModel(
    private val dataSource: UserRemoteDataSource,
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
            is UserProfileAction.OnFirstNameChange ->
                _state.update { it.copy(firstName = action.firstName) }
            is UserProfileAction.OnLastNameChange ->
                _state.update { it.copy(lastName = action.lastName) }
            is UserProfileAction.OnPhoneNumberChange ->
                _state.update { it.copy(phoneNumber = action.phoneNumber) }
            is UserProfileAction.OnAddressChange ->
                _state.update { it.copy(address = action.address) }
            is UserProfileAction.OnSaveClick -> save()
            is UserProfileAction.OnLogoutClick -> logout()
        }
    }

    private fun loadUserProfile() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            dataSource.getCurrentUser()
                .onSuccess { user ->
                    _state.update {
                        it.copy(
                            user = user,
                            isLoading = false,
                            firstName = user.firstName,
                            lastName = user.lastName,
                            phoneNumber = user.phoneNumber,
                            address = user.address,
                        )
                    }
                }
                .onFailure {
                    _state.update { it.copy(isLoading = false) }
                }
        }
    }

    private fun save() {
        val currentState = _state.value

        _state.update { it.copy(isSaving = true) }

        viewModelScope.launch {
            dataSource.updateCurrentUser(
                firstName = currentState.firstName,
                lastName = currentState.lastName,
                phoneNumber = currentState.phoneNumber,
                address = currentState.address,
            )
                .onSuccess { user ->
                    _state.update { it.copy(
                        user = user,
                        isSaving = false,
                        firstName = currentState.firstName,
                        lastName = currentState.lastName,
                        phoneNumber = currentState.phoneNumber,
                        address = currentState.address,
                    ) }
                }
                .onFailure {
                    _state.update { it.copy(isLoading = false) }
                }
        }
    }

    private fun logout() {
        viewModelScope.launch {
            _events.send(UserProfileEvent.LoggedOut)
        }
    }
}