package org.tcl.app.di

import org.koin.dsl.module
import org.tcl.app.core.data.network.BackendApiClient
import org.tcl.app.core.data.SecureStorage

val coreModule = module {
    single { SecureStorage(get()) }
    single { BackendApiClient(get()) }
}