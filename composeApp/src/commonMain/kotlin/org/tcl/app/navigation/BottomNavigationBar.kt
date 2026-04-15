package org.tcl.app.navigation

import androidx.compose.runtime.Composable
import zed.rainxch.rikkaui.components.ui.icon.RikkaIcons
import zed.rainxch.rikkaui.components.ui.navigationbar.NavigationBar
import zed.rainxch.rikkaui.components.ui.navigationbar.NavigationBarAnimation
import zed.rainxch.rikkaui.components.ui.navigationbar.NavigationBarItem

@Composable
fun BottomNavigationBar(
    onNavigate: (AppGraph) -> Unit,
    current: AppGraph,
) {
    NavigationBar {
        NavigationBarItem(
            selected = current is AppGraph.BookingList,
            onClick = { onNavigate(AppGraph.BookingList) },
            icon = RikkaIcons.Copy,
            label = "Buchungen",
            animation = NavigationBarAnimation.Tween,
        )
        NavigationBarItem(
            selected = current is AppGraph.BookingCourt,
            onClick = { onNavigate(AppGraph.BookingCourt) },
            icon = RikkaIcons.Star,
            label = "Plätze",
            animation = NavigationBarAnimation.Tween,
        )
    }
}