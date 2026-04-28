package org.tcl.app.onboarding.presentation.membership

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

class OnboardingMembershipViewModel(
    private val userRemoteDataSource: UserRemoteDataSource,
) : ViewModel() {

    private val _state = MutableStateFlow(OnboardingMembershipState())
    val state = _state.asStateFlow()

    private val _events = Channel<OnboardingMembershipEvent>()
    val events = _events.receiveAsFlow()

    fun onAction(action: OnboardingMembershipAction) {
        when (action) {
            is OnboardingMembershipAction.OnMemberToggle ->
                _state.update { it.copy(isMember = action.isMember) }
            OnboardingMembershipAction.OnNextClick -> save()
        }
    }

    private fun save() {
        _state.update { it.copy(isLoading = true, errorMessage = null) }

        viewModelScope.launch {
            userRemoteDataSource.updateCurrentUser(
                isMember = _state.value.isMember
            )
                .onSuccess {
                    _state.update { it.copy(isLoading = false) }
                    _events.send(OnboardingMembershipEvent.SavedSuccessfully)
                }
                .onFailure {
                    _state.update { it.copy(isLoading = false, errorMessage = "Speichern fehlgeschlagen") }
                }
        }
    }
}
