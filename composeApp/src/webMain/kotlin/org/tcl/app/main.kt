package org.tcl.app

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import com.mmk.kmpnotifier.notification.NotifierManager
import com.mmk.kmpnotifier.notification.configuration.NotificationPlatformConfiguration
import org.tcl.app.di.initKoin

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    NotifierManager.initialize(
        NotificationPlatformConfiguration.Web(
            askNotificationPermissionOnStart = true,
            notificationIconPath = null
        )
    )
    initKoin()
    ComposeViewport {
        App()
    }
}