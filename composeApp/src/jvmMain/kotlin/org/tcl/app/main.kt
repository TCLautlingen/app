package org.tcl.app

import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import org.koin.core.context.startKoin
import org.tcl.app.di.bookingModule
import org.tcl.app.di.platformModule

fun main() {
    startKoin {
        modules(platformModule, bookingModule)
    }
    application {
        val state = rememberWindowState(
            width = 393.dp,
            height = 852.dp,
        )
        Window(
            onCloseRequest = ::exitApplication,
            title = "app",
            state = state,
        ) {
            App()
        }
    }
}