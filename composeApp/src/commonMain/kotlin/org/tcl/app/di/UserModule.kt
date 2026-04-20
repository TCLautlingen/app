package org.tcl.app.di

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import org.tcl.app.user.data.UserApiService
import org.tcl.app.user.data.UserRepositoryImpl
import org.tcl.app.user.domain.UserRepository
import org.tcl.app.user.presentation.editor.UserEditorViewModel
import org.tcl.app.user.presentation.list.UserListViewModel
import org.tcl.app.user.presentation.profile.UserProfileViewModel

val userModule = module {
    single { UserApiService(get()) }
    single<UserRepository> { UserRepositoryImpl(get()) }

    viewModelOf(::UserProfileViewModel)
    viewModelOf(::UserListViewModel)
    viewModelOf(::UserEditorViewModel)
}