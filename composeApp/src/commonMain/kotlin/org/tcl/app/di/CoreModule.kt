package org.tcl.app.di

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import org.tcl.app.AppViewModel
import org.tcl.app.core.data.network.BackendApiClient

val coreModule = module {
    // SecureStorage is provided by each platform's platformModule
    single { BackendApiClient(get()) }
    // Dont use viewModelOf since we want just one instance of it to share with all screens
    singleOf(::AppViewModel)
}
