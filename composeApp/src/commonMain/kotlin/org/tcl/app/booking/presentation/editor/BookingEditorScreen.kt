package org.tcl.app.booking.presentation.editor

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import org.koin.compose.viewmodel.koinViewModel
import org.tcl.app.core.presentation.DateSheet
import org.tcl.app.core.presentation.ObserveAsEvents
import org.tcl.app.util.formatDdMmYyyy
import zed.rainxch.rikkaui.components.ui.button.Button
import zed.rainxch.rikkaui.components.ui.button.ButtonVariant
import zed.rainxch.rikkaui.components.ui.button.IconButton
import zed.rainxch.rikkaui.components.ui.icon.RikkaIcons
import zed.rainxch.rikkaui.components.ui.label.Label
import zed.rainxch.rikkaui.components.ui.scaffold.Scaffold
import zed.rainxch.rikkaui.components.ui.topappbar.TopAppBar
import zed.rainxch.rikkaui.foundation.RikkaTheme


@Composable
fun BookingEditorRoot(
    date: LocalDate? = null,
    courtId: Int? = null,
    startTime: LocalTime? = null,
    onCourtBooked: (
        date: LocalDate,
        startTime: LocalTime,
        durationMinutes: Int,
        courtName: String,
    ) -> Unit,
    onNavigateBack: () -> Unit,
    viewModel: BookingEditorViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(date, courtId, startTime) {
        viewModel.initialize(date, courtId, startTime)
    }

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is BookingEditorEvent.CourtBooked -> onCourtBooked(
                event.date,
                event.startTime,
                event.durationMinutes,
                event.courtName,
            )
        }
    }

    BookingEditorScreen(
        state = state,
        onAction = viewModel::onAction,
        onNavigateBack = onNavigateBack,
    )
}

@Composable
fun BookingEditorScreen(
    state: BookingEditorState,
    onAction: (BookingEditorAction) -> Unit,
    onNavigateBack: () -> Unit,
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = "Neue Buchung",
                navigationIcon = {
                    IconButton(
                        icon = RikkaIcons.ArrowLeft,
                        contentDescription = "Back",
                        onClick = onNavigateBack,
                    )
                }
            )
        },
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(RikkaTheme.spacing.lg),
            verticalArrangement = Arrangement.spacedBy(RikkaTheme.spacing.lg)
        ) {

            Column {
                Label(
                    text = "Datum wählen",
                )

                Button(
                    text = state.date.formatDdMmYyyy(),
                    onClick = { onAction(BookingEditorAction.OnDateClick) },
                    variant = ButtonVariant.Outline
                )
            }

            Column {
                Label(
                    text = "Dauer wählen",
                )

                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(RikkaTheme.spacing.sm),
                    verticalArrangement = Arrangement.spacedBy(0.dp),
                ) {
                    for (i in intArrayOf(30, 60, 90, 120)) {
                        Button(
                            onClick = { onAction(BookingEditorAction.OnDurationChange(i)) },
                            text = "$i min",
                            variant = if (i == state.duration) ButtonVariant.Default else ButtonVariant.Outline,
                        )
                    }
                }
            }

            Column {
                Label(
                    text = "Startzeit wählen",
                )

                val availableTimes = state.availableSlots.map { LocalTime.parse(it.startTime) }.distinct()

                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(RikkaTheme.spacing.sm),
                    verticalArrangement = Arrangement.spacedBy(0.dp),
                ) {
                    for (availableTime in availableTimes) {
                        Button(
                            onClick = { onAction(BookingEditorAction.OnStartTimeChange(availableTime)) },
                            text = availableTime.toString(),
                            variant = if (availableTime == state.startTime) ButtonVariant.Default else ButtonVariant.Outline,
                        )
                    }
                }
            }

            Column {
                Label(
                    text = "Platz wählen",
                )

                val availableCourts = state.availableSlots
                    .filter { LocalTime.parse(it.startTime) == state.startTime }
                    .map { it.court }

                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(RikkaTheme.spacing.sm),
                    verticalArrangement = Arrangement.spacedBy(0.dp),
                ) {
                    for (availableCourt in availableCourts) {
                        Button(
                            onClick = { onAction(BookingEditorAction.OnCourtChange(availableCourt.id)) },
                            text = availableCourt.name,
                            variant = if (availableCourt.id == state.courtId) ButtonVariant.Default else ButtonVariant.Outline,
                        )
                    }
                }
            }

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    onClick = { onAction(BookingEditorAction.OnBookClick) },
                    text = "Buchen",
                    enabled = !state.isSaving
                )
            }
        }

        DateSheet(
            open = state.showDateSheet,
            onDismiss = { onAction(BookingEditorAction.OnDateChangeDismiss) },
            selectedDate = state.date,
            onDateSelected = {
                onAction(BookingEditorAction.OnDateChange(it))
            },
        )
    }
}