package org.tcl.app.booking.presentation.success

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.composeapp.generated.resources.Res
import io.github.alexzhirkevich.compottie.LottieCompositionSpec
import io.github.alexzhirkevich.compottie.animateLottieCompositionAsState
import io.github.alexzhirkevich.compottie.rememberLottieComposition
import io.github.alexzhirkevich.compottie.rememberLottiePainter
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import org.koin.compose.viewmodel.koinViewModel
import org.tcl.app.util.formatDdMmYyyy
import org.tcl.app.util.plusMinutes
import zed.rainxch.rikkaui.components.ui.button.Button
import zed.rainxch.rikkaui.components.ui.button.ButtonVariant
import zed.rainxch.rikkaui.components.ui.card.Card
import zed.rainxch.rikkaui.components.ui.card.CardContent
import zed.rainxch.rikkaui.components.ui.scaffold.Scaffold
import zed.rainxch.rikkaui.components.ui.text.Text
import zed.rainxch.rikkaui.components.ui.text.TextVariant
import zed.rainxch.rikkaui.foundation.RikkaTheme

@Composable
fun BookingSuccessRoot(
    date: LocalDate,
    startTime: LocalTime,
    durationMinutes: Int,
    courtName: String,
    onNavigateHome: () -> Unit,
    viewModel: BookingSuccessViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(date, startTime, durationMinutes, courtName) {
        viewModel.initialize(date, startTime, durationMinutes, courtName)
    }

    BookingSuccessScreen(
        state = state,
        onAction = viewModel::onAction,
        onNavigateHome = onNavigateHome,
    )
}

@Composable
fun BookingSuccessScreen(
    state: BookingSuccessState,
    onAction: (BookingSuccessAction) -> Unit,
    onNavigateHome: () -> Unit,
) {
    val composition by rememberLottieComposition {
        LottieCompositionSpec.JsonString(
            Res.readBytes("files/Confetti.json").decodeToString()
        )
    }
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = 1,
    )

    val contentAlpha = remember { Animatable(0f) }
    val contentOffset = remember { Animatable(20f) }
    LaunchedEffect(Unit) {
        contentAlpha.animateTo(
            targetValue = 1f,
            animationSpec = spring(stiffness = Spring.StiffnessLow),
        )
    }
    LaunchedEffect(Unit) {
        contentOffset.animateTo(
            targetValue = 0f,
            animationSpec = spring(stiffness = Spring.StiffnessLow),
        )
    }

    Scaffold(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = rememberLottiePainter(
                    composition = composition,
                    progress = { progress },
                ),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(RikkaTheme.spacing.lg),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {

                Column(
                    modifier = Modifier.graphicsLayer {
                        alpha = contentAlpha.value
                        translationY = contentOffset.value
                    },
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(RikkaTheme.spacing.sm),
                ) {
                    Text(
                        text = "Platz gebucht!",
                        variant = TextVariant.H1,
                    )
                    Text(
                        text = "Viel Spaß beim Spielen.",
                        variant = TextVariant.Lead,
                        color = RikkaTheme.colors.onMuted,
                    )
                }

                Spacer(modifier = Modifier.height(RikkaTheme.spacing.xl))

                BookingDetailCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .graphicsLayer {
                            alpha = contentAlpha.value
                            translationY = contentOffset.value
                        },
                    state = state,
                )

                Spacer(modifier = Modifier.height(RikkaTheme.spacing.lg))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .graphicsLayer {
                            alpha = contentAlpha.value
                            translationY = contentOffset.value
                        },
                    verticalArrangement = Arrangement.spacedBy(RikkaTheme.spacing.sm),
                ) {
                    Button(
                        text = "Zum Kalender hinzufügen",
                        onClick = { onAction(BookingSuccessAction.OnAddToCalendarClick) },
                        modifier = Modifier.fillMaxWidth(),
                    )
                    Button(
                        text = "Zur Startseite",
                        onClick = onNavigateHome,
                        modifier = Modifier.fillMaxWidth(),
                        variant = ButtonVariant.Outline,
                    )
                }
            }
        }
    }
}

@Composable
private fun BookingDetailCard(
    modifier: Modifier = Modifier,
    state: BookingSuccessState,
) {
    val endTime = state.startTime.plusMinutes(state.durationMinutes)

    Card(modifier = modifier) {
        CardContent {
            Column(verticalArrangement = Arrangement.spacedBy(RikkaTheme.spacing.sm)) {
                BookingDetailRow(label = "Datum", value = state.date.formatDdMmYyyy())
                BookingDetailRow(label = "Uhrzeit", value = "${state.startTime} – $endTime")
                BookingDetailRow(label = "Platz", value = state.courtName)
                BookingDetailRow(label = "Dauer", value = "${state.durationMinutes} Minuten")
            }
        }
    }
}

@Composable
private fun BookingDetailRow(
    label: String,
    value: String,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = label,
            variant = TextVariant.Small,
            color = RikkaTheme.colors.onMuted,
        )
        Text(
            text = value,
            variant = TextVariant.Small,
        )
    }
}