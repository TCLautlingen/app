package org.tcl.app.notification.presentation.inbox

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel
import org.tcl.app.notification.BroadcastNotification
import org.tcl.app.util.formatRelative
import zed.rainxch.rikkaicons.core.DecorativeAppIcon
import zed.rainxch.rikkaicons.tokens.Bell
import zed.rainxch.rikkaui.components.ui.button.IconButton
import zed.rainxch.rikkaui.components.ui.card.Card
import zed.rainxch.rikkaui.components.ui.card.CardContent
import zed.rainxch.rikkaui.components.ui.card.CardHeader
import zed.rainxch.rikkaui.components.ui.icon.RikkaIcons
import zed.rainxch.rikkaui.components.ui.scaffold.Scaffold
import zed.rainxch.rikkaui.components.ui.spinner.Spinner
import zed.rainxch.rikkaui.components.ui.text.Text
import zed.rainxch.rikkaui.components.ui.text.TextVariant
import zed.rainxch.rikkaui.components.ui.topappbar.TopAppBar
import zed.rainxch.rikkaui.foundation.RikkaTheme

@Composable
fun NotificationInboxRoot(
    onNavigateBack: () -> Unit,
    viewModel: NotificationInboxViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        viewModel.onAction(NotificationInboxAction.OnRefresh)
    }

    NotificationInboxScreen(
        state = state,
        onNavigateBack = onNavigateBack,
    )
}

@Composable
fun NotificationInboxScreen(
    state: NotificationInboxState,
    onNavigateBack: () -> Unit,
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = "Posteingang",
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
        when {
            state.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    Spinner()
                }
            }
            state.notifications.isEmpty() -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = "Keine Nachrichten",
                        variant = TextVariant.Lead,
                        color = RikkaTheme.colors.onMuted,
                    )
                }
            }
            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(RikkaTheme.spacing.lg),
                    verticalArrangement = Arrangement.spacedBy(RikkaTheme.spacing.md),
                ) {
                    items(
                        items = state.notifications,
                        key = { it.id },
                    ) { notification ->
                        BroadcastNotificationCard(notification = notification)
                    }
                }
            }
        }
    }
}

@Composable
private fun BroadcastNotificationCard(notification: BroadcastNotification) {
    Card(modifier = Modifier.fillMaxWidth()) {
        CardHeader {
            Row(
                horizontalArrangement = Arrangement.spacedBy(RikkaTheme.spacing.sm),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier
                        .clip(RikkaTheme.shapes.lg)
                        .background(color = RikkaTheme.colors.primary)
                        .padding(RikkaTheme.spacing.sm)
                ) {
                    DecorativeAppIcon(
                        token = zed.rainxch.rikkaicons.tokens.RikkaIcons.Bell,
                        tint = RikkaTheme.colors.onPrimary,
                    )
                }

                Column {
                    Text(
                        text = notification.title,
                        variant = TextVariant.Large,
                    )
                    Text(
                        text = "${notification.createdByFirstName} ${notification.createdByLastName} · ${notification.createdAt.formatRelative()}",
                        variant = TextVariant.Small,
                        color = RikkaTheme.colors.onMuted,
                    )
                }
            }
        }
        CardContent {
            Text(
                text = notification.body,
                variant = TextVariant.Small,
            )
        }
    }
}
