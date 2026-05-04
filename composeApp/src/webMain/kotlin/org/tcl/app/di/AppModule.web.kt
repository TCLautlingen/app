package org.tcl.app.di

import org.koin.core.module.Module
import org.koin.dsl.module
import org.tcl.app.core.data.SecureStorage
import org.tcl.app.core.data.WebSecureStorage

actual val platformModule: Module = module {
    single<SecureStorage> {
        WebSecureStorage()
    }
}
