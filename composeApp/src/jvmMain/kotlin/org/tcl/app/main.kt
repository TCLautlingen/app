package org.tcl.app

import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.mmk.kmpnotifier.extensions.composeDesktopResourcesPath
import com.mmk.kmpnotifier.notification.NotifierManager
import com.mmk.kmpnotifier.notification.configuration.NotificationPlatformConfiguration
import org.tcl.app.di.initKoin
import java.io.File

fun main() {
    NotifierManager.initialize(
        NotificationPlatformConfiguration.Desktop(
            showPushNotification = true,
            notificationIconPath = composeDesktopResourcesPath() + File.separator + "ic_notification.png"
        )
    )
    initKoin()
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