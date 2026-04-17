package org.tcl.app.user.presentation.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel
import org.tcl.app.User
import org.tcl.app.navigation.AppGraph
import zed.rainxch.rikkaui.components.ui.avatar.Avatar
import zed.rainxch.rikkaui.components.ui.avatar.AvatarAnimation
import zed.rainxch.rikkaui.components.ui.avatar.AvatarSize
import zed.rainxch.rikkaui.components.ui.button.Button
import zed.rainxch.rikkaui.components.ui.button.ButtonVariant
import zed.rainxch.rikkaui.components.ui.button.IconButton
import zed.rainxch.rikkaui.components.ui.icon.RikkaIcons
import zed.rainxch.rikkaui.components.ui.input.Input
import zed.rainxch.rikkaui.components.ui.scaffold.Scaffold
import zed.rainxch.rikkaui.components.ui.spinner.Spinner
import zed.rainxch.rikkaui.components.ui.text.Text
import zed.rainxch.rikkaui.components.ui.topappbar.TopAppBar
import zed.rainxch.rikkaui.foundation.RikkaTheme

@Composable
fun UserListRoot(
    onNavigate: (AppGraph) -> Unit,
    onNavigateBack: () -> Unit,
    viewModel : UserListViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    UserListScreen(
        state = state,
        onAction = viewModel::onAction,
        onNavigate = onNavigate,
        onNavigateBack = onNavigateBack,
    )
}

@Composable
fun UserListScreen(
    state: UserListState,
    onAction: (UserListAction) -> Unit,
    onNavigate: (AppGraph) -> Unit,
    onNavigateBack: () -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = "Mitglieder",
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
            Input(
                value = state.searchQuery,
                onValueChange = { onAction(UserListAction.OnSearchQueryChange(it)) }
            )

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
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(RikkaTheme.spacing.md),
                    ) {
                        items(
                            items = state.users,
                            key = { it.id },
                        ) { user ->
                            UserItem(
                                user = user,
                                onClick = { onNavigate(AppGraph.UserEditor(user.id)) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun UserItem(
    user: User,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        variant = ButtonVariant.Ghost
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(RikkaTheme.spacing.md),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Avatar(
                fallback =  "${user.firstName.first()}${user.lastName.first()}",
                size = AvatarSize.Default,
                animation = AvatarAnimation.Scale,
            )

            Column() {
                Text(text = "${user.firstName} ${user.lastName}")
                Text(text = user.email)
            }
        }
    }
}