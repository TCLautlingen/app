package org.tcl.app.onboarding.presentation.contact

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
import org.tcl.app.user.UpdateUserRequest
import org.tcl.app.user.domain.UserRemoteDataSource

class OnboardingContactViewModel(
    private val userRemoteDataSource: UserRemoteDataSource,
) : ViewModel() {

    private val _state = MutableStateFlow(OnboardingContactState())
    val state = _state.asStateFlow()

    private val _events = Channel<OnboardingContactEvent>()
    val events = _events.receiveAsFlow()

    fun onAction(action: OnboardingContactAction) {
        when (action) {
            is OnboardingContactAction.OnPhoneNumberChange ->
                _state.update { it.copy(phoneNumber = action.phoneNumber) }
            is OnboardingContactAction.OnAddressChange ->
                _state.update { it.copy(address = action.address) }
            OnboardingContactAction.OnNextClick -> save()
        }
    }

    private fun save() {
        val s = _state.value

        _state.update { it.copy(isLoading = true, errorMessage = null) }

        viewModelScope.launch {
            userRemoteDataSource.updateCurrentUser(
                phoneNumber = s.phoneNumber.trim().ifBlank { null },
                address = s.address.trim().ifBlank { null },
            )
                .onSuccess {
                    _state.update { it.copy(isLoading = false) }
                    _events.send(OnboardingContactEvent.SavedSuccessfully)
                }
                .onFailure {
                    _state.update { it.copy(isLoading = false, errorMessage = "Speichern fehlgeschlagen") }
                }
        }
    }
}
