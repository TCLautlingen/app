package org.tcl.app.di

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import org.tcl.app.repositories.BookingRepository
import org.tcl.app.repositories.CourtRepository
import org.tcl.app.repositories.DeviceRepository
import org.tcl.app.repositories.FakeBookingRepository
import org.tcl.app.repositories.FakeCourtRepository
import org.tcl.app.repositories.FakeDeviceRepository
import org.tcl.app.repositories.FakeNotificationRepository
import org.tcl.app.repositories.FakeRefreshTokenRepository
import org.tcl.app.repositories.FakeUserRepository
import org.tcl.app.repositories.NotificationRepository
import org.tcl.app.repositories.PostgresBookingRepository
import org.tcl.app.repositories.PostgresCourtRepository
import org.tcl.app.repositories.PostgresDeviceRepository
import org.tcl.app.repositories.PostgresNotificationRepository
import org.tcl.app.repositories.PostgresRefreshTokenRepository
import org.tcl.app.repositories.PostgresUserRepository
import org.tcl.app.repositories.RefreshTokenRepository
import org.tcl.app.repositories.UserRepository
import org.tcl.app.services.BookingService
import org.tcl.app.services.CourtService
import org.tcl.app.services.FirebaseService
import org.tcl.app.services.NotificationService
import org.tcl.app.services.SlotService
import org.tcl.app.services.UserService

const val TESTING = true

val appModule = module {
    single<UserRepository> {
        if (TESTING) FakeUserRepository()
        else PostgresUserRepository()
    }
    single<BookingRepository> {
        if (TESTING) FakeBookingRepository(get())
        else PostgresBookingRepository()
    }
    single<RefreshTokenRepository> {
        if (TESTING) FakeRefreshTokenRepository()
        else PostgresRefreshTokenRepository()
    }
    single<DeviceRepository> {
        if (TESTING) FakeDeviceRepository()
        else PostgresDeviceRepository()
    }
    single<CourtRepository> {
        if (TESTING) FakeCourtRepository()
        else PostgresCourtRepository()
    }
    single<NotificationRepository> {
        if (TESTING) FakeNotificationRepository()
        else PostgresNotificationRepository()
    }

    singleOf(::UserService)
    singleOf(::BookingService)
    singleOf(::SlotService)
    singleOf(::CourtService)
    singleOf(::FirebaseService)
    singleOf(::NotificationService)
}
