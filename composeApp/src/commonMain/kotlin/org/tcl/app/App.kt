package org.tcl.app

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.tcl.app.navigation.AppNavigation
import zed.rainxch.rikkaui.foundation.RikkaPalette
import zed.rainxch.rikkaui.foundation.RikkaTheme

@Composable
fun App() {
    val colors = RikkaPalette.Zinc.resolve(isDark = false)

    RikkaTheme(colors = colors) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(RikkaTheme.colors.background)
                .safeDrawingPadding(),
        ) {
            AppNavigation()
        }
    }
}