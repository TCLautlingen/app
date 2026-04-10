package org.tcl.app

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.tcl.app.di.bookingModule
import org.tcl.app.di.platformModule

class AppApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@AppApplication)
            modules(platformModule, bookingModule)
        }
    }
}
