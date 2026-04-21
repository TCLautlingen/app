package org.tcl.app.notification.presentation.builder

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel
import org.tcl.app.core.presentation.ObserveAsEvents
import zed.rainxch.rikkaui.components.ui.button.Button
import zed.rainxch.rikkaui.components.ui.button.IconButton
import zed.rainxch.rikkaui.components.ui.icon.RikkaIcons
import zed.rainxch.rikkaui.components.ui.input.Input
import zed.rainxch.rikkaui.components.ui.label.Label
import zed.rainxch.rikkaui.components.ui.scaffold.Scaffold
import zed.rainxch.rikkaui.components.ui.text.Text
import zed.rainxch.rikkaui.components.ui.textarea.Textarea
import zed.rainxch.rikkaui.components.ui.topappbar.TopAppBar
import zed.rainxch.rikkaui.foundation.RikkaTheme

@Composable
fun NotificationBuilderRoot(
    onNavigateBack: () -> Unit,
    viewModel: NotificationBuilderViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is NotificationBuilderEvent.NotificationSent -> onNavigateBack()
        }
    }

    NotificationBuilderScreen(
        state = state,
        onAction = viewModel::onAction,
        onNavigateBack = onNavigateBack
    )
}

@Composable
fun NotificationBuilderScreen(
    state: NotificationBuilderState,
    onAction: (NotificationBuilderAction) -> Unit,
    onNavigateBack: () -> Unit,
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = "Rundnachricht erstellen",
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
                .padding(RikkaTheme.spacing.lg),
            verticalArrangement = Arrangement.spacedBy(RikkaTheme.spacing.lg)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(RikkaTheme.spacing.sm)
            ) {
                Label(
                    text = "Überschrift",
                )
                Input(
                    value = state.title,
                    onValueChange = { onAction(NotificationBuilderAction.OnTitleChange(it)) },
                    label = "Überschrift",
                )
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(RikkaTheme.spacing.sm)
            ) {
                Label(
                    text = "Inhalt",
                )
                Textarea(
                    value = state.body,
                    onValueChange = { onAction(NotificationBuilderAction.OnBodyChange(it)) },
                    label = "Inhalt",
                )
            }

            Button(
                text = "Nachricht senden",
                onClick = { onAction(NotificationBuilderAction.OnSendClick) },
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}