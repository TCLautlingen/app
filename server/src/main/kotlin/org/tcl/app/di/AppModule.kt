package org.tcl.app.di

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import org.tcl.app.auth.FakeRefreshTokenRepository
import org.tcl.app.auth.PostgresRefreshTokenRepository
import org.tcl.app.auth.RefreshTokenRepository
import org.tcl.app.booking.BookingRepository
import org.tcl.app.booking.BookingService
import org.tcl.app.booking.FakeBookingRepository
import org.tcl.app.booking.PostgresBookingRepository
import org.tcl.app.court.CourtRepository
import org.tcl.app.court.CourtService
import org.tcl.app.court.FakeCourtRepository
import org.tcl.app.court.PostgresCourtRepository
import org.tcl.app.device.DeviceRepository
import org.tcl.app.device.FakeDeviceRepository
import org.tcl.app.device.PostgresDeviceRepository
import org.tcl.app.firebase.FirebaseService
import org.tcl.app.notification.FakeNotificationRepository
import org.tcl.app.notification.NotificationRepository
import org.tcl.app.notification.NotificationService
import org.tcl.app.notification.PostgresNotificationRepository
import org.tcl.app.slot.SlotService
import org.tcl.app.user.FakeUserRepository
import org.tcl.app.user.PostgresUserRepository
import org.tcl.app.user.UserRepository
import org.tcl.app.user.UserService

private const val TESTING = true

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