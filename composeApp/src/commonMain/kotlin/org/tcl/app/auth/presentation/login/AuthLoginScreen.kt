package org.tcl.app.auth.presentation.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel
import org.tcl.app.core.presentation.ObserveAsEvents
import zed.rainxch.rikkaui.components.ui.button.Button
import zed.rainxch.rikkaui.components.ui.button.IconButton
import zed.rainxch.rikkaui.components.ui.icon.RikkaIcons
import zed.rainxch.rikkaui.components.ui.input.Input
import zed.rainxch.rikkaui.components.ui.label.Label
import zed.rainxch.rikkaui.components.ui.scaffold.Scaffold
import zed.rainxch.rikkaui.components.ui.text.Text
import zed.rainxch.rikkaui.components.ui.topappbar.TopAppBar
import zed.rainxch.rikkaui.foundation.RikkaTheme

@Composable
fun AuthLoginRoot(
    onNavigateBack: () -> Unit,
    onSuccess: () -> Unit,
    viewModel: AuthLoginViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is AuthLoginEvent.LoggedIn -> onSuccess()
        }
    }

    AuthLoginScreen(
        onNavigateBack = onNavigateBack,
        state = state,
        onAction = viewModel::onAction,
    )
}

@Composable
fun AuthLoginScreen(
    onNavigateBack: () -> Unit,
    state: AuthLoginState,
    onAction: (AuthLoginAction) -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = "Anmelden",
                navigationIcon = {
                    IconButton(
                        icon = RikkaIcons.ArrowLeft,
                        contentDescription = "Back",
                        onClick = onNavigateBack,
                    )
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(RikkaTheme.spacing.lg),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(RikkaTheme.spacing.lg),
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(RikkaTheme.spacing.sm)
                ) {
                    Label(
                        text = "Email",
                        required = true
                    )
                    Input(
                        value = state.email,
                        onValueChange = { onAction(AuthLoginAction.OnEmailChange(it)) },
                        placeholder = "maxmustermann@beispiel.de",
                        label = "Email",
                    )
                }

                Column(
                    verticalArrangement = Arrangement.spacedBy(RikkaTheme.spacing.sm)
                ) {
                    Label(
                        text = "Passwort",
                        required = true
                    )
                    Input(
                        value = state.password,
                        onValueChange = { onAction(AuthLoginAction.OnPasswordChange(it)) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        visualTransformation = PasswordVisualTransformation(),
                        label = "Passwort",
                    )
                }

                Text(
                    text = state.errorMessage ?: "",
                    color = RikkaTheme.colors.destructive,
                )
            }

            Button(
                text = "Weiter",
                onClick = { onAction(AuthLoginAction.OnLoginClick) },
                enabled = !state.isLoading,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}