package org.tcl.app.onboarding.presentation.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.tcl.app.auth.RegisterError
import org.tcl.app.auth.RegisterErrorCode
import org.tcl.app.auth.RegisterField
import org.tcl.app.auth.RegisterValidator
import org.tcl.app.auth.domain.AuthRemoteDataSource
import org.tcl.app.core.data.SecureStorage
import org.tcl.app.core.domain.util.onFailure
import org.tcl.app.core.domain.util.onSuccess

class OnboardingAccountViewModel(
    private val authRemoteDataSource: AuthRemoteDataSource,
    private val secureStorage: SecureStorage,
) : ViewModel() {

    private val _state = MutableStateFlow(OnboardingAccountState())
    val state = _state.asStateFlow()

    private val _events = Channel<OnboardingAccountEvent>()
    val events = _events.receiveAsFlow()

    fun onAction(action: OnboardingAccountAction) {
        when (action) {
            is OnboardingAccountAction.OnEmailChange ->
                _state.update { it.copy(email = action.email, emailError = null) }
            is OnboardingAccountAction.OnPasswordChange ->
                _state.update { it.copy(password = action.password, passwordError = null, confirmPasswordError = null) }
            is OnboardingAccountAction.OnConfirmPasswordChange ->
                _state.update { it.copy(confirmPassword = action.confirmPassword, confirmPasswordError = null) }
            is OnboardingAccountAction.OnTermsCheck ->
                _state.update { it.copy(termsChecked = action.checked) }
            OnboardingAccountAction.OnNextClick -> register()
        }
    }

    private fun register() {
        val currentState = _state.value

        _state.update { it.copy(
            isLoading = true,
            emailError = null,
            passwordError = null,
            confirmPasswordError = null,
            termsError = null,
        ) }

        if (!currentState.termsChecked) {
            _state.update { it.copy(termsError = "Nutzungsbedingen wurden nicht akzeptiert") }
        }

        val hasConfirmPasswordError = currentState.password != currentState.confirmPassword
        if (hasConfirmPasswordError) {
            _state.update { it.copy(confirmPasswordError = "Passwörter stimmen nicht überein") }
        }

        val localErrors = RegisterValidator.validate(currentState.email, currentState.password)
        val hasLocalErrors = localErrors.isNotEmpty()
        if (hasLocalErrors) {
            updateErrors(localErrors)
        }

        if (!currentState.termsChecked || hasConfirmPasswordError || hasLocalErrors) {
            _state.update { it.copy(isLoading = false) }
            return
        }

        _state.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            authRemoteDataSource.register(email = currentState.email, password = currentState.password)
                .onSuccess { tokens ->
                    secureStorage.tokens = tokens
                    _state.update { it.copy(isLoading = false) }
                    _events.send(OnboardingAccountEvent.RegisteredSuccessfully)
                }
                .onFailure { error ->
                    updateErrors(error.errors)
                    _state.update { it.copy(isLoading = false) }
                }
        }
    }

    private fun updateErrors(errors: List<RegisterError>) {
        for (error in errors) {
            val text = when (error.code) {
                RegisterErrorCode.EMAIL_INVALID -> "E-Mail-Adresse ist ungültig"
                RegisterErrorCode.EMAIL_TOO_LONG -> "E-Mail-Adresse ist zu lang"
                RegisterErrorCode.EMAIL_ALREADY_EXISTS -> "Diese E-Mail-Adresse ist bereits registriert"
                RegisterErrorCode.PASSWORD_TOO_SHORT -> "Passwort muss mindestens ${error.params["min"]} Zeichen lang sein"
                RegisterErrorCode.PASSWORD_TOO_LONG -> "Passwort ist zu lang"
                RegisterErrorCode.PASSWORD_MISSING_DIGIT -> "Passwort muss mindestens eine Zahl enthalten"
            }

            when (error.field) {
                RegisterField.EMAIL -> _state.update { it.copy(emailError = text) }
                RegisterField.PASSWORD -> _state.update { it.copy(passwordError = text) }
            }
        }
    }
}