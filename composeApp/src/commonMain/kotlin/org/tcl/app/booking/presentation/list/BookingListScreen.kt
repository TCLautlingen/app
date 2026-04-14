package org.tcl.app.booking.presentation.list

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import app.composeapp.generated.resources.Res
import app.composeapp.generated.resources.drone
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import org.tcl.app.navigation.AppGraph
import org.tcl.app.navigation.BottomNavigationBar
import zed.rainxch.rikkaui.components.ui.alertdialog.AlertDialog
import zed.rainxch.rikkaui.components.ui.alertdialog.AlertDialogAction
import zed.rainxch.rikkaui.components.ui.alertdialog.AlertDialogActionVariant
import zed.rainxch.rikkaui.components.ui.alertdialog.AlertDialogAnimation
import zed.rainxch.rikkaui.components.ui.alertdialog.AlertDialogCancel
import zed.rainxch.rikkaui.components.ui.alertdialog.AlertDialogFooter
import zed.rainxch.rikkaui.components.ui.alertdialog.AlertDialogHeader
import zed.rainxch.rikkaui.components.ui.button.Button
import zed.rainxch.rikkaui.components.ui.button.ButtonVariant
import zed.rainxch.rikkaui.components.ui.button.IconButton
import zed.rainxch.rikkaui.components.ui.card.Card
import zed.rainxch.rikkaui.components.ui.card.CardContent
import zed.rainxch.rikkaui.components.ui.fab.Fab
import zed.rainxch.rikkaui.components.ui.icon.RikkaIcons
import zed.rainxch.rikkaui.components.ui.scaffold.Scaffold
import zed.rainxch.rikkaui.components.ui.spinner.Spinner
import zed.rainxch.rikkaui.components.ui.text.Text
import zed.rainxch.rikkaui.components.ui.text.TextVariant
import zed.rainxch.rikkaui.components.ui.topappbar.TopAppBar
import zed.rainxch.rikkaui.foundation.RikkaTheme

@Composable
fun BookingListRoot(
    onNavigate: (AppGraph) -> Unit,
    currentRoute: AppGraph?,
    viewModel: BookingListViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    BookingListScreen(
        state = state,
        onAction = viewModel::onAction,
        onNavigate = onNavigate,
        currentRoute = currentRoute
    )
}

@Composable
fun BookingListScreen(
    state: BookingListState,
    onAction: (BookingListAction) -> Unit,
    onNavigate: (AppGraph) -> Unit,
    currentRoute: AppGraph?,
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(lifecycleOwner.lifecycle) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
            onAction(BookingListAction.OnRefresh)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = "Meine Buchungen")
        },
        bottomBar = {
            BottomNavigationBar(
                onNavigate = onNavigate,
                current = currentRoute ?: AppGraph.BookingList,
            )
        },
        floatingActionButton = {
            Fab(
                icon = RikkaIcons.Plus,
                label = "buchen",
                onClick = { onNavigate(AppGraph.CreateBooking) },
            )
        },
    ) { when {
            state.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    Spinner()
                }
            }
            state.bookings.isEmpty() -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Noch keinen Platz gebucht",
                            variant = TextVariant.Lead,
                            color = RikkaTheme.colors.onMuted,
                        )
                        Spacer(modifier = Modifier.height(RikkaTheme.spacing.lg))
                        Button(
                            text = "Buche einen Platz",
                            onClick = { onNavigate(AppGraph.CreateBooking) },
                            variant = ButtonVariant.Outline,
                        )
                    }
                }
            }
            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(RikkaTheme.spacing.lg),
                    verticalArrangement = Arrangement.spacedBy(RikkaTheme.spacing.md),
                ) {
                    items(
                        items = state.bookings,
                        key = { it.id },
                    ) { booking ->
                        BookingCard(
                            booking = booking,
                            onDelete = { onAction(BookingListAction.OnDeleteClick(booking.id)) },
                        )
                    }
                }
            }
        }


        AlertDialog(
            modifier = Modifier.padding(RikkaTheme.spacing.xl),
            open = state.showDeleteDialog,
            onDismiss = { onAction(BookingListAction.OnDismissDeleteDialog) },
            onConfirm = { onAction(BookingListAction.OnConfirmDelete) },
            animation = AlertDialogAnimation.FadeScale,
        ) {
            AlertDialogHeader(
                title = "Are you absolutely sure?",
                description = "This action cannot be undone.",
            )
            AlertDialogFooter {
                AlertDialogCancel(onClick = { onAction(BookingListAction.OnDismissDeleteDialog) })
                AlertDialogAction(
                    text = "Delete",
                    onClick = { onAction(BookingListAction.OnConfirmDelete) },
                    variant = AlertDialogActionVariant.Destructive,
                )
            }
        }
    }
}

@Composable
fun BookingCard(
    booking: BookingUi,
    onDelete: () -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
    ) {
        CardContent {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(RikkaTheme.spacing.lg),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(RikkaTheme.colors.primary)
                ) {
                    Image(
                        painter = painterResource(Res.drawable.drone),
                        contentDescription = "Platz${booking.court}",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Am ${booking.date} um ${booking.startTime}",
                        variant = TextVariant.Small,
                    )
                    Text(
                        text = "${booking.duration} Minuten auf Platz ${booking.court}",
                        variant = TextVariant.Small,
                    )
                }
                IconButton(
                    icon = RikkaIcons.Trash,
                    contentDescription = "Delete booking",
                    onClick = onDelete,
                )
            }
        }
    }
}