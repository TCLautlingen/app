package org.tcl.app.onboarding.presentation.welcome

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import app.composeapp.generated.resources.Res
import app.composeapp.generated.resources.tennis_court
import org.jetbrains.compose.resources.painterResource
import org.tcl.app.navigation.AppGraph
import zed.rainxch.rikkaui.components.ui.button.Button
import zed.rainxch.rikkaui.components.ui.button.ButtonVariant
import zed.rainxch.rikkaui.components.ui.scaffold.Scaffold
import zed.rainxch.rikkaui.components.ui.text.Text
import zed.rainxch.rikkaui.components.ui.text.TextVariant
import zed.rainxch.rikkaui.foundation.RikkaTheme

@Composable
fun OnboardingWelcomeRoot(
    onNavigate: (AppGraph) -> Unit,
) {
    OnboardingWelcomeScreen(onNavigate)
}

@Composable
fun OnboardingWelcomeScreen(
    onNavigate: (AppGraph) -> Unit,
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
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
                verticalArrangement = Arrangement.spacedBy(RikkaTheme.spacing.sm, Alignment.CenterVertically),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Image(
                    painter = painterResource(Res.drawable.tennis_court),
                    contentDescription = "Tennisplatz",
                    modifier = Modifier
                        .widthIn(max = 240.dp)
                )

                Text(
                    text = "TENNISCLUB LAUTLINGEN",
                    variant = TextVariant.Small,
                    color = RikkaTheme.colors.onMuted,
                    textAlign = TextAlign.Center,
                )
                Text(
                    text = "Willkommen auf dem Platz.",
                    variant = TextVariant.H1,
                    textAlign = TextAlign.Center,
                )
                Text(
                    text = "Buche deinen Platz, finde Mitspieler und behalte deinen Club im Blick.",
                    variant = TextVariant.Small,
                    color = RikkaTheme.colors.onMuted,
                    textAlign = TextAlign.Center,
                )
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(RikkaTheme.spacing.sm),
            ) {
                Button(
                    text = "Loslegen",
                    onClick = {
                        onNavigate(AppGraph.OnboardingAccount)
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                Button(
                    text = "Ich habe schon ein Konto",
                    onClick = { },
                    modifier = Modifier.fillMaxWidth(),
                    variant = ButtonVariant.Ghost
                )
            }
        }
    }
}