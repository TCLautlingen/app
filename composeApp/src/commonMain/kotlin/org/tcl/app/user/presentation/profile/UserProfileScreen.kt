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
                            .fillMaxWidth()
                            .padding(RikkaTheme.spacing.lg),
                        verticalArrangement = Arrangement.spacedBy(RikkaTheme.spacing.sm),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Avatar(
                            fallback = "${state.user?.firstName?.firstOrNull()}${state.user?.lastName?.firstOrNull()}",
                            size = AvatarSize.Lg
                        )
                        Text(
                            text = state.user?.firstName + " " + state.user?.lastName,
                            variant = TextVariant.Large
                        )
                        Text(
                            text = state.user?.email ?: "max.mustermann@email.de",
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