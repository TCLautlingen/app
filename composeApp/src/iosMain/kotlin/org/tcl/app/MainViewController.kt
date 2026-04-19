package org.tcl.app

import androidx.compose.ui.window.ComposeUIViewController
import org.koin.core.context.startKoin
import org.tcl.app.di.authModule
import org.tcl.app.di.bookingModule
import org.tcl.app.di.coreModule
import org.tcl.app.di.courtModule
import org.tcl.app.di.platformModule
import org.tcl.app.di.userModule
import platform.UIKit.UIViewController

fun MainViewController(): UIViewController {
    initKoin()
    return ComposeUIViewController { App() }
}

fun initKoin() {
    startKoin {
        modules(platformModule, coreModule, authModule, bookingModule, userModule, courtModule)
    }
}