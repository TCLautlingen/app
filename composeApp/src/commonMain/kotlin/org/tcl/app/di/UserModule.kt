package org.tcl.app.di

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import org.tcl.app.user.data.FakeUserRemoteDataSource
import org.tcl.app.user.data.KtorUserRemoteDataSource
import org.tcl.app.user.domain.UserRemoteDataSource
import org.tcl.app.user.presentation.editor.UserEditorViewModel
import org.tcl.app.user.presentation.list.UserListViewModel
import org.tcl.app.user.presentation.profile.UserProfileViewModel

val userModule = module {
    single<UserRemoteDataSource> {
        if (TESTING) FakeUserRemoteDataSource()
        else KtorUserRemoteDataSource(get())
    }

    viewModelOf(::UserProfileViewModel)
    viewModelOf(::UserListViewModel)
    viewModelOf(::UserEditorViewModel)
}