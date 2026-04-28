package org.tcl.app.onboarding.presentation.membership

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import org.tcl.app.navigation.AppGraph
import zed.rainxch.rikkaui.components.ui.button.Button
import zed.rainxch.rikkaui.components.ui.button.IconButton
import zed.rainxch.rikkaui.components.ui.card.Card
import zed.rainxch.rikkaui.components.ui.card.CardContent
import zed.rainxch.rikkaui.components.ui.icon.RikkaIcons
import zed.rainxch.rikkaui.components.ui.scaffold.Scaffold
import zed.rainxch.rikkaui.components.ui.spinner.Spinner
import zed.rainxch.rikkaui.components.ui.text.Text
import zed.rainxch.rikkaui.components.ui.text.TextVariant
import zed.rainxch.rikkaui.components.ui.toggle.Toggle
import zed.rainxch.rikkaui.components.ui.topappbar.TopAppBar
import zed.rainxch.rikkaui.foundation.RikkaTheme

@Composable
fun OnboardingMembershipRoot(
    onNavigateBack: () -> Unit,
    onNavigate: (AppGraph) -> Unit,
    viewModel: OnboardingMembershipViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            OnboardingMembershipEvent.SavedSuccessfully -> onNavigate(AppGraph.OnboardingProfile)
        }
    }

    OnboardingMembershipScreen(
        state = state,
        onAction = viewModel::onAction,
        onNavigateBack = onNavigateBack,
    )
}

@Composable
fun OnboardingMembershipScreen(
    state: OnboardingMembershipState,
    onAction: (OnboardingMembershipAction) -> Unit,
    onNavigateBack: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = "Mitgliedschaft",
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
                Text(
                    text = "Bist du Mitglied im Tennisclub Lautlingen?",
                    variant = TextVariant.Lead,
                )
                Text(
                    text = "Als Mitglied kannst du Plätze buchen und an Club-Aktivitäten teilnehmen. Falls du noch kein Mitglied bist, kann deine Mitgliedschaft durch einen Administrator bestätigt werden.",
                    color = RikkaTheme.colors.onMuted,
                )

                Card(modifier = Modifier.fillMaxWidth()) {
                    CardContent {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(text = "Ich bin Mitglied")
                            Toggle(
                                checked = state.isMember,
                                onCheckedChange = { onAction(OnboardingMembershipAction.OnMemberToggle(it)) },
                            )
                        }
                    }
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
                onClick = { onAction(OnboardingMembershipAction.OnNextClick) },
                enabled = !state.isLoading,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}
