package org.tcl.app.notification.presentation.builder

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel
import zed.rainxch.rikkaui.components.ui.button.Button
import zed.rainxch.rikkaui.components.ui.input.Input
import zed.rainxch.rikkaui.components.ui.label.Label
import zed.rainxch.rikkaui.components.ui.scaffold.Scaffold
import zed.rainxch.rikkaui.components.ui.textarea.Textarea
import zed.rainxch.rikkaui.foundation.RikkaTheme

@Composable
fun NotificationBuilderRoot(
    viewModel: NotificationBuilderViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    NotificationBuilderScreen(
        state = state,
        onAction = viewModel::onAction,
    )
}

@Composable
fun NotificationBuilderScreen(
    state: NotificationBuilderState,
    onAction: (NotificationBuilderAction) -> Unit,
) {
    Scaffold(modifier = Modifier.fillMaxSize()) {
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
                onClick = { onAction(NotificationBuilderAction.OnSendClick) },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    text = "Nachricht senden",
                )
            }
        }
    }
}