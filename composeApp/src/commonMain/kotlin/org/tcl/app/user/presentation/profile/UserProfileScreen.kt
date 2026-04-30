package org.tcl.app.user.presentation.profile

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel
import org.tcl.app.core.presentation.ObserveAsEvents
import org.tcl.app.navigation.AppGraph
import org.tcl.app.navigation.BottomNavigationBar
import zed.rainxch.rikkaui.components.ui.avatar.Avatar
import zed.rainxch.rikkaui.components.ui.avatar.AvatarSize
import zed.rainxch.rikkaui.components.ui.button.Button
import zed.rainxch.rikkaui.components.ui.button.ButtonVariant
import zed.rainxch.rikkaui.components.ui.button.IconButton
import zed.rainxch.rikkaui.components.ui.icon.RikkaIcons
import zed.rainxch.rikkaui.components.ui.input.Input
import zed.rainxch.rikkaui.components.ui.label.Label
import zed.rainxch.rikkaui.components.ui.scaffold.Scaffold
import zed.rainxch.rikkaui.components.ui.spinner.Spinner
import zed.rainxch.rikkaui.components.ui.text.Text
import zed.rainxch.rikkaui.components.ui.text.TextVariant
import zed.rainxch.rikkaui.components.ui.topappbar.TopAppBar
import zed.rainxch.rikkaui.foundation.RikkaTheme


@Composable
fun UserProfileRoot(
    onNavigate: (AppGraph) -> Unit,
    currentRoute: AppGraph?,
    onLoggedOut: () -> Unit,
    viewModel: UserProfileViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is UserProfileEvent.LoggedOut -> onLoggedOut()
        }
    }

    UserProfileScreen(
        state = state,
        onAction = viewModel::onAction,
        onNavigate = onNavigate,
        currentRoute = currentRoute,
    )
}

@Composable
fun UserProfileScreen(
    state: UserProfileState,
    onAction: (UserProfileAction) -> Unit,
    onNavigate: (AppGraph) -> Unit,
    currentRoute: AppGraph?,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = "Profil",
                actions = {
                    IconButton(
                        icon = RikkaIcons.Mail,
                        contentDescription = "Notifications",
                        onClick = { onNavigate(AppGraph.NotificationInbox) },
                    )
                    if (state.user?.isAdmin ?: false) {
                        IconButton(
                            icon = RikkaIcons.Send,
                            contentDescription = "Send Notification",
                            onClick = { onNavigate(AppGraph.NotificationBuilder) },
                        )
                        IconButton(
                            icon = RikkaIcons.User,
                            contentDescription = "User List",
                            onClick = { onNavigate(AppGraph.UserList) },
                        )
                    }
                }
            )
        },
        bottomBar = {
            BottomNavigationBar(
                onNavigate = onNavigate,
                current = currentRoute ?: AppGraph.BookingList,
            )
        }
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
            else -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(RikkaTheme.spacing.lg),
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Column(
                        modifier = Modifier
                            .weight(1f),
                        verticalArrangement = Arrangement.spacedBy(RikkaTheme.spacing.sm),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Avatar(
                            fallback = "${state.user?.firstName?.firstOrNull()}${state.user?.lastName?.firstOrNull()}",
                            size = AvatarSize.Lg
                        )
                        Text(
                            text = state.user?.email ?: "max.mustermann@email.de",
                        )

                        Spacer(Modifier.height(RikkaTheme.spacing.xl))

                        Column(verticalArrangement = Arrangement.spacedBy(RikkaTheme.spacing.sm)) {
                            Label(text = "Vorname", required = true)
                            Input(
                                value = state.firstName,
                                onValueChange = { onAction(UserProfileAction.OnFirstNameChange(it)) },
                                placeholder = "Max",
                                label = "Vorname",
                            )
                        }

                        Column(verticalArrangement = Arrangement.spacedBy(RikkaTheme.spacing.sm)) {
                            Label(text = "Nachname", required = true)
                            Input(
                                value = state.lastName,
                                onValueChange = { onAction(UserProfileAction.OnLastNameChange(it)) },
                                placeholder = "Mustermann",
                                label = "Nachname",
                            )
                        }

                        Column(verticalArrangement = Arrangement.spacedBy(RikkaTheme.spacing.sm)) {
                            Label(text = "Telefonnummer")
                            Input(
                                value = state.phoneNumber ?: "",
                                onValueChange = { onAction(UserProfileAction.OnPhoneNumberChange(it)) },
                                placeholder = "+49 123 456789",
                                label = "Telefonnummer",
                            )
                        }

                        Column(verticalArrangement = Arrangement.spacedBy(RikkaTheme.spacing.sm)) {
                            Label(text = "Adresse")
                            Input(
                                value = state.address ?: "",
                                onValueChange = { onAction(UserProfileAction.OnAddressChange(it)) },
                                placeholder = "Musterstraße 1, 72459 Albstadt",
                                label = "Adresse",
                            )
                        }

                        Button(
                            text = "Speichern",
                            onClick = { onAction(UserProfileAction.OnSaveClick) },
                        )
                    }

                    Button(
                        text = "Abmelden",
                        onClick = {
                            onAction(UserProfileAction.OnLogoutClick)
                        },
                        variant = ButtonVariant.Destructive
                    )
                }
            }
        }
    }
}