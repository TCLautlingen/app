package org.tcl.app.di

import org.koin.dsl.module
import org.tcl.app.core.data.ApiClient
import org.tcl.app.core.data.TokenManager

val coreModule = module {
    single { TokenManager(get()) }
    single { ApiClient(get()) }
    single { get<ApiClient>().client }
}