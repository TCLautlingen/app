package org.tcl.app

import androidx.compose.ui.window.ComposeUIViewController
import org.koin.core.context.startKoin
import org.tcl.app.di.authModule
import org.tcl.app.di.bookingModule
import org.tcl.app.di.coreModule
import org.tcl.app.di.platformModule

fun MainViewController() = ComposeUIViewController { App() }

fun initKoin() {
    startKoin {
        modules(platformModule, coreModule, authModule, bookingModule)
    }
}
