package org.tcl.app

import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import org.koin.core.context.startKoin
import org.tcl.app.di.authModule
import org.tcl.app.di.bookingModule
import org.tcl.app.di.coreModule
import org.tcl.app.di.courtModule
import org.tcl.app.di.platformModule
import org.tcl.app.di.userModule

fun main() {
    startKoin {
        modules(platformModule, coreModule, authModule, bookingModule, userModule, courtModule)
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