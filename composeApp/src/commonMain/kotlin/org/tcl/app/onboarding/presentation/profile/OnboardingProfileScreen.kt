package org.tcl.app.onboarding.presentation.profile

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.tcl.app.navigation.AppGraph
import zed.rainxch.rikkaui.components.ui.button.Button
import zed.rainxch.rikkaui.components.ui.button.IconButton
import zed.rainxch.rikkaui.components.ui.icon.RikkaIcons
import zed.rainxch.rikkaui.components.ui.scaffold.Scaffold
import zed.rainxch.rikkaui.components.ui.topappbar.TopAppBar
import zed.rainxch.rikkaui.foundation.RikkaTheme

@Composable
fun OnboardingProfileRoot(
    onNavigateBack: () -> Unit,
    onNavigate: (AppGraph) -> Unit
) {
    OnboardingProfileScreen(
        onNavigateBack = onNavigateBack,
        onNavigate = onNavigate,
    )
}

@Composable
fun OnboardingProfileScreen(
    onNavigateBack: () -> Unit,
    onNavigate: (AppGraph) -> Unit,
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

            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(RikkaTheme.spacing.sm),
            ) {
                Button(
                    text = "Weiter",
                    onClick = { onNavigate(AppGraph.OnboardingContact) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}