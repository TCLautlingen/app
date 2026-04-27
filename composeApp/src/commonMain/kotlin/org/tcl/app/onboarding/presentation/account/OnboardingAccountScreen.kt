package org.tcl.app.onboarding.presentation.account

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.tcl.app.auth.presentation.AuthAction
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
fun OnboardingAccountRoot(
    onNavigateBack: () -> Unit,
    onNavigate: (AppGraph) -> Unit
) {
    OnboardingAccountScreen(
        onNavigateBack = onNavigateBack,
        onNavigate = onNavigate,
    )
}

@Composable
fun OnboardingAccountScreen(
    onNavigateBack: () -> Unit,
    onNavigate: (AppGraph) -> Unit,
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
                Column(
                    verticalArrangement = Arrangement.spacedBy(RikkaTheme.spacing.sm)
                ) {
                    Label(
                        text = "Email",
                        required = true
                    )
                    Input(
                        value = "",
                        onValueChange = {  },
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
                        value = "",
                        onValueChange = {  },
                        label = "Passwort",
                    )
                }

                Column(
                    verticalArrangement = Arrangement.spacedBy(RikkaTheme.spacing.sm)
                ) {
                    Label(
                        text = "Passwort wiederholen",
                        required = true
                    )
                    Input(
                        value = "",
                        onValueChange = {  },
                        label = "Passwort wiederholen",
                    )
                }

                Text(
                    text = "",
                    color = RikkaTheme.colors.destructive,
                )
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(RikkaTheme.spacing.sm),
            ) {
                Button(
                    text = "Weiter",
                    onClick = { onNavigate(AppGraph.OnboardingMembership) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}