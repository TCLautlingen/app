package org.tcl.app.onboarding.presentation.account

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel
import org.tcl.app.core.presentation.ObserveAsEvents
import org.tcl.app.navigation.AppGraph
import zed.rainxch.rikkaui.components.ui.button.Button
import zed.rainxch.rikkaui.components.ui.button.IconButton
import zed.rainxch.rikkaui.components.ui.checkbox.Checkbox
import zed.rainxch.rikkaui.components.ui.icon.RikkaIcons
import zed.rainxch.rikkaui.components.ui.input.Input
import zed.rainxch.rikkaui.components.ui.label.Label
import zed.rainxch.rikkaui.components.ui.scaffold.Scaffold
import zed.rainxch.rikkaui.components.ui.text.Text
import zed.rainxch.rikkaui.components.ui.topappbar.TopAppBar
import zed.rainxch.rikkaui.foundation.RikkaTheme

@Composable
fun OnboardingAccountRoot(
    onNavigateBack: () -> Unit,
    onNavigate: (AppGraph) -> Unit,
    onRegistered: () -> Unit,
    viewModel: OnboardingAccountViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            OnboardingAccountEvent.RegisteredSuccessfully -> {
                onRegistered()
                onNavigate(AppGraph.OnboardingProfile)
            }
        }
    }

    OnboardingAccountScreen(
        state = state,
        onAction = viewModel::onAction,
        onNavigateBack = onNavigateBack,
    )
}

@Composable
fun OnboardingAccountScreen(
    state: OnboardingAccountState,
    onAction: (OnboardingAccountAction) -> Unit,
    onNavigateBack: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = "Konto erstellen",
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
                Column(verticalArrangement = Arrangement.spacedBy(RikkaTheme.spacing.sm)) {
                    Label(text = "Email", required = true)
                    Input(
                        value = state.email,
                        onValueChange = { onAction(OnboardingAccountAction.OnEmailChange(it)) },
                        placeholder = "maxmustermann@beispiel.de",
                        label = "Email",
                    )
                    Text(text = state.emailError?: "", color = RikkaTheme.colors.destructive)
                }

                Column(verticalArrangement = Arrangement.spacedBy(RikkaTheme.spacing.sm)) {
                    Label(text = "Passwort", required = true)
                    Input(
                        value = state.password,
                        onValueChange = { onAction(OnboardingAccountAction.OnPasswordChange(it)) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        visualTransformation = PasswordVisualTransformation(),
                        label = "Passwort",
                    )
                    Text(text = state.passwordError ?: "", color = RikkaTheme.colors.destructive)
                }

                Column(verticalArrangement = Arrangement.spacedBy(RikkaTheme.spacing.sm)) {
                    Label(text = "Passwort wiederholen", required = true)
                    Input(
                        value = state.confirmPassword,
                        onValueChange = { onAction(OnboardingAccountAction.OnConfirmPasswordChange(it)) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        visualTransformation = PasswordVisualTransformation(),
                        label = "Passwort wiederholen",
                    )
                    Text(text = state.confirmPasswordError ?: "", color = RikkaTheme.colors.destructive)
                }

                Column(verticalArrangement = Arrangement.spacedBy(RikkaTheme.spacing.sm)) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(RikkaTheme.spacing.sm),
                    ) {
                        Checkbox(
                            checked = state.termsChecked,
                            onCheckedChange ={ onAction(OnboardingAccountAction.OnTermsCheck(it)) },
                        )
                        FlowRow(
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Text(
                                text = "Ich stimme den "
                            )
                            Text(
                                text = "Nutzungsbedingungen",
                                modifier = Modifier.clickable { },
                                style = TextStyle(textDecoration = TextDecoration.Underline),
                            )
                            Text(
                                text = "und der "
                            )
                            Text(
                                text = "Datenschutzerklärung",
                                modifier = Modifier.clickable { },
                                style = TextStyle(textDecoration = TextDecoration.Underline),
                            )
                            Text(
                                text = " zu."
                            )
                        }
                    }

                    Text(text = state.termsError ?: "", color = RikkaTheme.colors.destructive)
                }
            }

            Button(
                text = "Weiter",
                onClick = { onAction(OnboardingAccountAction.OnNextClick) },
                enabled = !state.isLoading,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}
