package org.tcl.app.di

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import org.tcl.app.notification.data.FakeNotificationRemoteDataSource
import org.tcl.app.notification.data.KtorNotificationRemoteDataSource
import org.tcl.app.notification.domain.NotificationRemoteDataSource
import org.tcl.app.notification.presentation.builder.NotificationBuilderViewModel
import org.tcl.app.notification.presentation.inbox.NotificationInboxViewModel

val notificationModule = module {
    single { KtorNotificationRemoteDataSource(get()) }
    single<NotificationRemoteDataSource> {
        if (TESTING) FakeNotificationRemoteDataSource()
        else KtorNotificationRemoteDataSource(get())
    }

    viewModelOf(::NotificationBuilderViewModel)
    viewModelOf(::NotificationInboxViewModel)
}