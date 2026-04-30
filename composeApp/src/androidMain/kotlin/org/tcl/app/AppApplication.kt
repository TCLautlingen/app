package org.tcl.app

import android.app.Application
import android.content.Context
import com.mmk.kmpnotifier.notification.NotifierManager
import com.mmk.kmpnotifier.notification.configuration.NotificationPlatformConfiguration
import org.koin.android.ext.koin.androidContext
import org.tcl.app.di.initKoin

object AppContext {
    lateinit var application: Application

    fun get(): Context = application
}

class AppApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        AppContext.application = this

        NotifierManager.initialize(
            configuration = NotificationPlatformConfiguration.Android(
                notificationIconResId = R.drawable.ic_launcher_foreground,
                showPushNotification = true,
            )
        )

        initKoin {
            androidContext(this@AppApplication)
        }
    }
}
