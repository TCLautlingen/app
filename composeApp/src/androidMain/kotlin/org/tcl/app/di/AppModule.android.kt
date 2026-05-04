package org.tcl.app.di

import eu.anifantakis.lib.ksafe.KSafe
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.Module
import org.koin.dsl.module
import org.tcl.app.core.data.KSafeSecureStorage
import org.tcl.app.core.data.SecureStorage

actual val platformModule: Module = module {
    single<SecureStorage> {
        KSafeSecureStorage(KSafe(androidApplication()))
    }
}
