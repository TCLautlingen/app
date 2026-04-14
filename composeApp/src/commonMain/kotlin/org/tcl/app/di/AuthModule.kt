package org.tcl.app.di

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import org.tcl.app.auth.data.AuthApiService
import org.tcl.app.auth.domain.AuthRepository
import org.tcl.app.auth.presentation.AuthViewModel

val authModule = module {
    single { AuthApiService(get()) }
    single { AuthRepository(get()) }

    viewModelOf(::AuthViewModel)
}