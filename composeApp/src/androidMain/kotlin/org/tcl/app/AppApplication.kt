package org.tcl.app

import android.app.Application
import android.content.Context
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.tcl.app.di.authModule
import org.tcl.app.di.bookingModule
import org.tcl.app.di.coreModule
import org.tcl.app.di.courtModule
import org.tcl.app.di.platformModule
import org.tcl.app.di.userModule

object AppContext {
    lateinit var application: Application

    fun get(): Context = application
}

class AppApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        AppContext.application = this
        startKoin {
            androidContext(this@AppApplication)
            modules(platformModule, coreModule, authModule, bookingModule, userModule, courtModule)
        }
    }
}
