package org.tcl.app.booking.presentation.court

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel
import org.tcl.app.COURT_COUNT
import org.tcl.app.navigation.AppGraph
import org.tcl.app.navigation.BottomNavigationBar
import zed.rainxch.rikkaui.components.ui.button.Button
import zed.rainxch.rikkaui.components.ui.button.ButtonVariant
import zed.rainxch.rikkaui.components.ui.scaffold.Scaffold
import zed.rainxch.rikkaui.components.ui.text.Text
import zed.rainxch.rikkaui.components.ui.topappbar.TopAppBar
import zed.rainxch.rikkaui.foundation.RikkaTheme


@Composable
fun BookingCourtRoot(
    onNavigate: (AppGraph) -> Unit,
    currentRoute: AppGraph?,
    viewModel: BookingCourtViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    BookingCourtScreen(
        state = state,
        onAction = viewModel::onAction,
        onNavigate = onNavigate,
        currentRoute = currentRoute,
    )
}

@Composable
fun BookingCourtScreen(
    state: BookingCourtState,
    onAction: (BookingCourtAction) -> Unit,
    onNavigate: (AppGraph) -> Unit,
    currentRoute: AppGraph?,
) {
    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        onAction(BookingCourtAction.OnRefresh)
    }

    Scaffold(
        topBar = {
            TopAppBar(title = "Platz-Übersicht")
        },
        bottomBar = {
            BottomNavigationBar(
                onNavigate = onNavigate,
                current = currentRoute ?: AppGraph.BookingList,
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(RikkaTheme.spacing.lg),
            verticalArrangement = Arrangement.spacedBy(RikkaTheme.spacing.lg)
        ) {
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(RikkaTheme.spacing.sm),
                verticalArrangement = Arrangement.spacedBy(0.dp),
            ) {
                for (i in 1..COURT_COUNT) {
                    Button(
                        onClick = { onAction(BookingCourtAction.OnCourtChange(i)) },
                        text = "Platz $i",
                        variant = if (i == state.court) ButtonVariant.Default else ButtonVariant.Outline,
                    )
                }
            }

            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(1.dp),
            ) {
                for (courtSlot in state.courtSlots) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(if (courtSlot.taken) RikkaTheme.colors.destructive else RikkaTheme.colors.success)
                            .clickable(
                                enabled = !courtSlot.taken
                            ) {
                                onNavigate(AppGraph.CreateBooking(
                                    date = state.date,
                                    court = state.court,
                                    startTime = courtSlot.startTime
                                ))
                            }
                            .padding(RikkaTheme.spacing.md),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = courtSlot.startTime,
                        )
                        if (!courtSlot.taken) {
                            Text(
                                text = "→",
                            )
                        }
                    }
                }
            }
        }
    }
}