package org.tcl.app.user.presentation.editor

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel
import org.tcl.app.core.presentation.ObserveAsEvents
import zed.rainxch.rikkaui.components.ui.button.IconButton
import zed.rainxch.rikkaui.components.ui.icon.RikkaIcons
import zed.rainxch.rikkaui.components.ui.input.Input
import zed.rainxch.rikkaui.components.ui.label.Label
import zed.rainxch.rikkaui.components.ui.scaffold.Scaffold
import zed.rainxch.rikkaui.components.ui.spinner.Spinner
import zed.rainxch.rikkaui.components.ui.toggle.Toggle
import zed.rainxch.rikkaui.components.ui.topappbar.TopAppBar
import zed.rainxch.rikkaui.foundation.RikkaTheme

@Composable
fun UserEditorRoot(
    userId: Int,
    onNavigateBack: () -> Unit,
    viewModel: UserEditorViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is UserEditorEvent.UserSaved -> onNavigateBack()
        }
    }

    LaunchedEffect(userId) {
        viewModel.initialize(userId)
    }

    UserEditorScreen(
        state = state,
        onAction = viewModel::onAction,
        onNavigateBack = onNavigateBack,
    )
}

@Composable
fun UserEditorScreen(
    state: UserEditorState,
    onAction: (UserEditorAction) -> Unit,
    onNavigateBack: () -> Unit,
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = "Mitglied bearbeiten",
                navigationIcon = {
                    IconButton(
                        icon = RikkaIcons.ArrowLeft,
                        contentDescription = "Back",
                        onClick = onNavigateBack,
                    )
                },
                actions = {
                    IconButton(
                        icon = RikkaIcons.Check,
                        contentDescription = "Save",
                        onClick = { onAction(UserEditorAction.OnSaveClick) },
                    )
                }
            )
        },
    ) {
        when (state.user) {
            null -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    Spinner()
                }
            }
            else -> {
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
                            text = "Email",
                        )
                        Input(
                            value = state.user.email,
                            onValueChange = {  },
                            label = "Email",
                            enabled = false,
                        )
                    }

                    Column(
                        verticalArrangement = Arrangement.spacedBy(RikkaTheme.spacing.sm)
                    ) {
                        Label(
                            text = "Vorname",
                        )
                        Input(
                            value = state.user.firstName,
                            onValueChange = {  },
                            label = "Vorname",
                            enabled = false,
                        )
                    }

                    Column(
                        verticalArrangement = Arrangement.spacedBy(RikkaTheme.spacing.sm)
                    ) {
                        Label(
                            text = "Nachname",
                        )
                        Input(
                            value = state.user.lastName,
                            onValueChange = {  },
                            label = "Nachname",
                            enabled = false,
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Label(text = "Vereinsmitglied")
                        Toggle(
                            checked = state.isMember,
                            onCheckedChange = { onAction(UserEditorAction.OnMemberToggle(it)) },
                            label = "Vereinsmitglied",
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Label(text = "Admin")
                        Toggle(
                            checked = state.isAdmin,
                            onCheckedChange = { onAction(UserEditorAction.OnAdminToggle(it)) },
                            label = "Admin",
                        )
                    }
                }
            }
        }
    }
}