package org.tcl.app.onboarding.presentation.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel
import org.tcl.app.core.presentation.ObserveAsEvents
import zed.rainxch.rikkaui.components.ui.button.Button
import zed.rainxch.rikkaui.components.ui.button.IconButton
import zed.rainxch.rikkaui.components.ui.icon.RikkaIcons
import zed.rainxch.rikkaui.components.ui.input.Input
import zed.rainxch.rikkaui.components.ui.label.Label
import zed.rainxch.rikkaui.components.ui.scaffold.Scaffold
import zed.rainxch.rikkaui.components.ui.spinner.Spinner
import zed.rainxch.rikkaui.components.ui.text.Text
import zed.rainxch.rikkaui.components.ui.topappbar.TopAppBar
import zed.rainxch.rikkaui.foundation.RikkaTheme

@Composable
fun OnboardingProfileRoot(
    onNavigateBack: () -> Unit,
    onComplete: () -> Unit,
    viewModel: OnboardingProfileViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            OnboardingProfileEvent.SavedSuccessfully -> onComplete()
        }
    }

    OnboardingProfileScreen(
        state = state,
        onAction = viewModel::onAction,
        onNavigateBack = onNavigateBack,
    )
}

@Composable
fun OnboardingProfileScreen(
    state: OnboardingProfileState,
    onAction: (OnboardingProfileAction) -> Unit,
    onNavigateBack: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = "Profil erstellen",
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
                    Label(text = "Vorname", required = true)
                    Input(
                        value = state.firstName,
                        onValueChange = { onAction(OnboardingProfileAction.OnFirstNameChange(it)) },
                        placeholder = "Max",
                        label = "Vorname",
                    )
                    if (state.firstNameError != null) {
                        Text(text = state.firstNameError, color = RikkaTheme.colors.destructive)
                    }
                }

                Column(verticalArrangement = Arrangement.spacedBy(RikkaTheme.spacing.sm)) {
                    Label(text = "Nachname", required = true)
                    Input(
                        value = state.lastName,
                        onValueChange = { onAction(OnboardingProfileAction.OnLastNameChange(it)) },
                        placeholder = "Mustermann",
                        label = "Nachname",
                    )
                    if (state.lastNameError != null) {
                        Text(text = state.lastNameError, color = RikkaTheme.colors.destructive)
                    }
                }

                Column(verticalArrangement = Arrangement.spacedBy(RikkaTheme.spacing.sm)) {
                    Label(text = "Telefonnummer")
                    Input(
                        value = state.phoneNumber,
                        onValueChange = { onAction(OnboardingProfileAction.OnPhoneNumberChange(it)) },
                        placeholder = "+49 123 456789",
                        label = "Telefonnummer",
                    )
                }

                Column(verticalArrangement = Arrangement.spacedBy(RikkaTheme.spacing.sm)) {
                    Label(text = "Adresse")
                    Input(
                        value = state.address,
                        onValueChange = { onAction(OnboardingProfileAction.OnAddressChange(it)) },
                        placeholder = "Musterstraße 1, 72459 Albstadt",
                        label = "Adresse",
                    )
                }

                if (state.errorMessage != null) {
                    Text(text = state.errorMessage, color = RikkaTheme.colors.destructive)
                }

                if (state.isLoading) {
                    Spinner()
                }
            }

            Button(
                text = "Weiter",
                onClick = { onAction(OnboardingProfileAction.OnNextClick) },
                enabled = !state.isLoading,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}
