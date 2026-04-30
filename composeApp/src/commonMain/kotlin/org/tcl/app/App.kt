package org.tcl.app

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.tcl.app.core.presentation.AdaptiveLayout
import org.tcl.app.navigation.NavigationRoot
import zed.rainxch.rikkaicons.core.ProvideIconPack
import zed.rainxch.rikkaicons.pack.lucide.LucidePack
import zed.rainxch.rikkaui.foundation.RikkaAccentPreset
import zed.rainxch.rikkaui.foundation.RikkaPalette
import zed.rainxch.rikkaui.foundation.RikkaStylePreset
import zed.rainxch.rikkaui.foundation.RikkaTheme

@Composable
fun App() {
    RikkaTheme(
        palette = RikkaPalette.Zinc,
        accent = RikkaAccentPreset.Default,
        isDark = false,
        preset = RikkaStylePreset.Default,
    ) {
        ProvideIconPack(LucidePack) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(RikkaTheme.colors.background)
                    .safeDrawingPadding(),
            ) {
                AdaptiveLayout {
                    NavigationRoot()
                }
            }
        }
    }
}