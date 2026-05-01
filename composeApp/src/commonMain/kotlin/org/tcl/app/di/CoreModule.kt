package org.tcl.app.di

import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import org.tcl.app.AppViewModel
import org.tcl.app.core.data.SecureStorage
import org.tcl.app.core.data.network.BackendApiClient

val coreModule = module {
    single { SecureStorage(get()) }
    single { BackendApiClient(get()) }
    // Dont use viewModelOf since we want just one instance of it to share with all screens
    singleOf(::AppViewModel)
}