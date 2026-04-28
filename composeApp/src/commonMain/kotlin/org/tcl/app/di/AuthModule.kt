package org.tcl.app.di

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import org.tcl.app.auth.data.FakeAuthRemoteDataSource
import org.tcl.app.auth.data.KtorAuthRemoteDataSource
import org.tcl.app.auth.domain.AuthRemoteDataSource
import org.tcl.app.auth.presentation.login.AuthLoginViewModel

val authModule = module {
    single<AuthRemoteDataSource> {
        if (TESTING) FakeAuthRemoteDataSource()
        else KtorAuthRemoteDataSource(get())
    }

    viewModelOf(::AuthLoginViewModel)
}