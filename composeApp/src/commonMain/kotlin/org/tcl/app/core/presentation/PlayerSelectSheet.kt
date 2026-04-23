package org.tcl.app.core.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.tcl.app.user.User
import zed.rainxch.rikkaui.components.ui.avatar.Avatar
import zed.rainxch.rikkaui.components.ui.icon.Icon
import zed.rainxch.rikkaui.components.ui.icon.RikkaIcons
import zed.rainxch.rikkaui.components.ui.input.Input
import zed.rainxch.rikkaui.components.ui.sheet.*
import zed.rainxch.rikkaui.components.ui.text.Text
import zed.rainxch.rikkaui.foundation.RikkaTheme

@Composable
fun PlayerSelectSheet(
    open: Boolean,
    onDismiss: () -> Unit,
    onPlayerSelected: (User) -> Unit,
    selectedPlayerIds: List<Int>,
    players: List<User>,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
) {
    Sheet(
        open = open,
        onDismiss = onDismiss,
        side = SheetSide.Bottom,
        animation = SheetAnimation.Slide,
    ) {
        SheetHeader(title = "Mitspieler hinzufügen")
        SheetContent {
            Column(
                verticalArrangement = Arrangement.spacedBy(RikkaTheme.spacing.sm)
            ) {
                Text(
                    text = "${selectedPlayerIds.size} / 3 ausgewählt",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.End,
                )

                Input(
                    value = searchQuery,
                    onValueChange = onSearchQueryChange,
                    placeholder = "Name oder E-Mail suchen…",
                )

                LazyColumn(
                    modifier = Modifier.height(320.dp),
                    verticalArrangement = Arrangement.spacedBy(RikkaTheme.spacing.sm),
                ) {
                    items(players, key = { it.id }) { player ->
                        PlayerRow(
                            player = player,
                            isSelected = player.id in selectedPlayerIds,
                            onClick = { onPlayerSelected(player) },
                            enabled = player.id in selectedPlayerIds || selectedPlayerIds.size < 3,
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PlayerRow(
    player: User,
    isSelected: Boolean,
    onClick: () -> Unit,
    enabled: Boolean,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.medium)
            .clickable(enabled, onClick = onClick)
            .padding( RikkaTheme.spacing.sm),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(RikkaTheme.spacing.sm),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Avatar(
                fallback = "${player.firstName.firstOrNull() ?: "?"}${player.lastName.firstOrNull() ?: ""}",
            )

            Column {
                Text("${player.firstName} ${player.lastName}")
                Text(
                    text = player.email,
                )
            }
        }

        if (isSelected) {
            Icon(
                imageVector = RikkaIcons.Check,
                contentDescription = null,
                tint = RikkaTheme.colors.primary,
            )
        }
    }
}