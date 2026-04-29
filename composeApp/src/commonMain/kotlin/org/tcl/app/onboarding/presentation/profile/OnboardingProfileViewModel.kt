package org.tcl.app.onboarding.presentation.profile

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

class OnboardingProfileViewModel(
    private val userRemoteDataSource: UserRemoteDataSource,
) : ViewModel() {

    private val _state = MutableStateFlow(OnboardingProfileState())
    val state = _state.asStateFlow()

    private val _events = Channel<OnboardingProfileEvent>()
    val events = _events.receiveAsFlow()

    fun onAction(action: OnboardingProfileAction) {
        when (action) {
            is OnboardingProfileAction.OnFirstNameChange ->
                _state.update { it.copy(firstName = action.firstName, firstNameError = null) }
            is OnboardingProfileAction.OnLastNameChange ->
                _state.update { it.copy(lastName = action.lastName, lastNameError = null) }
            is OnboardingProfileAction.OnPhoneNumberChange ->
                _state.update { it.copy(phoneNumber = action.phoneNumber) }
            is OnboardingProfileAction.OnAddressChange ->
                _state.update { it.copy(address = action.address) }
            OnboardingProfileAction.OnNextClick -> save()
        }
    }

    private fun save() {
        val currentState = _state.value

        val firstNameError = if (currentState.firstName.isBlank()) "Vorname ist erforderlich" else null
        val lastNameError = if (currentState.lastName.isBlank()) "Nachname ist erforderlich" else null

        if (firstNameError != null || lastNameError != null) {
            _state.update { it.copy(firstNameError = firstNameError, lastNameError = lastNameError) }
            return
        }

        _state.update { it.copy(isLoading = true, errorMessage = null) }

        viewModelScope.launch {
            userRemoteDataSource.updateCurrentUser(
                firstName = currentState.firstName.trim(),
                lastName = currentState.lastName.trim(),
                phoneNumber = currentState.phoneNumber.trim().ifBlank { null },
                address = currentState.address.trim().ifBlank { null },
            )
                .onSuccess {
                    _state.update { it.copy(isLoading = false) }
                    _events.send(OnboardingProfileEvent.SavedSuccessfully)
                }
                .onFailure {
                    _state.update { it.copy(isLoading = false, errorMessage = "Speichern fehlgeschlagen") }
                }
        }
    }
}
