package org.tcl.app.booking.presentation.court

import androidx.compose.runtime.Composable
import org.tcl.app.navigation.AppGraph
import org.tcl.app.navigation.BottomNavigationBar
import zed.rainxch.rikkaui.components.ui.scaffold.Scaffold
import zed.rainxch.rikkaui.components.ui.topappbar.TopAppBar


@Composable
fun BookingCourtRoot(
    onNavigate: (AppGraph) -> Unit,
    currentRoute: AppGraph?,
) {
    BookingCourtScreen(
        onNavigate = onNavigate,
        currentRoute = currentRoute,
    )
}

@Composable
fun BookingCourtScreen(
    onNavigate: (AppGraph) -> Unit,
    currentRoute: AppGraph?,
) {
    Scaffold(
        topBar = {
            TopAppBar(title = "Platz-Übersicht")
        },
        bottomBar = {
            BottomNavigationBar(
                onNavigate = onNavigate,
                current = currentRoute ?: AppGraph.BookingList,
            )
        }
    ) {

    }
}