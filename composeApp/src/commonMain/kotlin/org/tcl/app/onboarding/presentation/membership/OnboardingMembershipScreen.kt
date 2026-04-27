package org.tcl.app.onboarding.presentation.membership

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.tcl.app.navigation.AppGraph
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
fun OnboardingMembershipRoot(
    onNavigateBack: () -> Unit,
    onNavigate: (AppGraph) -> Unit
) {
    OnboardingMembershipScreen(
        onNavigateBack = onNavigateBack,
        onNavigate = onNavigate,
    )
}

@Composable
fun OnboardingMembershipScreen(
    onNavigateBack: () -> Unit,
    onNavigate: (AppGraph) -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = "Mitgliedschaft bestätigen",
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

            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(RikkaTheme.spacing.sm),
            ) {
                Button(
                    text = "Weiter",
                    onClick = { onNavigate(AppGraph.OnboardingProfile) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}