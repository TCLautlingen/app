package org.tcl.app

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import org.koin.core.context.startKoin
import org.tcl.app.di.bookingModule
import org.tcl.app.di.platformModule

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    startKoin {
        modules(platformModule, bookingModule)
    }
    ComposeViewport {
        App()
    }
}