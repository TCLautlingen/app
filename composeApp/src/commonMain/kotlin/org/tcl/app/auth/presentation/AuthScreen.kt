package org.tcl.app.auth.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel
import org.tcl.app.core.presentation.ObserveAsEvents
import zed.rainxch.rikkaui.components.ui.button.Button
import zed.rainxch.rikkaui.components.ui.input.Input
import zed.rainxch.rikkaui.components.ui.label.Label
import zed.rainxch.rikkaui.components.ui.tabs.Tab
import zed.rainxch.rikkaui.components.ui.tabs.TabAnimation
import zed.rainxch.rikkaui.components.ui.tabs.TabContent
import zed.rainxch.rikkaui.components.ui.tabs.TabList
import zed.rainxch.rikkaui.foundation.RikkaTheme

@Composable
fun AuthRoot(
    onSuccess: () -> Unit,
    viewModel: AuthViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is AuthEvent.LoggedIn -> onSuccess()
            is AuthEvent.Registered -> onSuccess()
        }
    }

    AuthScreen(
        state = state,
        onAction = viewModel::onAction,
    )
}

@Composable
fun AuthScreen(
    state: AuthState,
    onAction: (AuthAction) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(RikkaTheme.spacing.xl),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TabList(
          modifier = Modifier.fillMaxWidth()
        ) {
            Tab(
                selected = state.selectedTab == 0,
                onClick = { onAction(AuthAction.OnTabChange(0)) },
                text = "Anmelden",
                animation = TabAnimation.Spring,
                modifier = Modifier.weight(1f)
            )
            Tab(
                selected = state.selectedTab == 1,
                onClick = { onAction(AuthAction.OnTabChange(1)) },
                text = "Registrieren",
                animation = TabAnimation.Spring,
                modifier = Modifier.weight(1f)
            )
        }

        TabContent {
            Column(
                verticalArrangement = Arrangement.spacedBy(RikkaTheme.spacing.lg)
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
                        onValueChange = { onAction(AuthAction.OnEmailChange(it)) },
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
                        onValueChange = { onAction(AuthAction.OnPasswordChange(it)) },
                        label = "Passwort",
                    )
                }

                if (state.selectedTab != 0) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(RikkaTheme.spacing.sm)
                    ) {
                        Label(
                            text = "Passwort wiederholen",
                            required = true
                        )
                        Input(
                            value = state.confirmPassword,
                            onValueChange = { onAction(AuthAction.OnConfirmPasswordChange(it)) },
                            label = "Passwort wiederholen",
                        )
                    }

                    Column(
                        verticalArrangement = Arrangement.spacedBy(RikkaTheme.spacing.sm)
                    ) {
                        Label(
                            text = "Vorname",
                            required = true
                        )
                        Input(
                            value = state.firstName,
                            onValueChange = { onAction(AuthAction.OnFirstNameChange(it)) },
                            label = "Vorname",
                        )
                    }

                    Column(
                        verticalArrangement = Arrangement.spacedBy(RikkaTheme.spacing.sm)
                    ) {
                        Label(
                            text = "Nachname",
                            required = true
                        )
                        Input(
                            value = state.lastName,
                            onValueChange = { onAction(AuthAction.OnLastNameChange(it)) },
                            label = "Nachname",
                        )
                    }
                }

                Button(
                    onClick = { if (state.selectedTab == 0) onAction(AuthAction.OnLoginClick) else onAction(AuthAction.OnRegisterClick) },
                    text = if (state.selectedTab == 0) "Anmelden" else "Registrieren",
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}