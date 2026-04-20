package org.tcl.app.di

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import org.tcl.app.notification.data.NotificationApiService
import org.tcl.app.notification.data.NotificationRepositoryImpl
import org.tcl.app.notification.domain.NotificationRepository
import org.tcl.app.notification.presentation.builder.NotificationBuilderViewModel

val notificationModule = module {
    single { NotificationApiService(get()) }
    single<NotificationRepository> { NotificationRepositoryImpl(get()) }

    viewModelOf(::NotificationBuilderViewModel)
}