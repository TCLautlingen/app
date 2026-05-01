package org.tcl.app

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import org.tcl.app.repositories.BookingRepository
import org.tcl.app.repositories.CourtRepository
import org.tcl.app.repositories.NotificationTokenRepository
import org.tcl.app.repositories.fake.FakeBookingRepository
import org.tcl.app.repositories.fake.FakeCourtRepository
import org.tcl.app.repositories.fake.FakeNotificationTokenRepository
import org.tcl.app.repositories.fake.FakeNotificationRepository
import org.tcl.app.repositories.fake.FakeRefreshTokenRepository
import org.tcl.app.repositories.fake.FakeUserRepository
import org.tcl.app.repositories.NotificationRepository
import org.tcl.app.repositories.postgres.PostgresBookingRepository
import org.tcl.app.repositories.postgres.PostgresCourtRepository
import org.tcl.app.repositories.postgres.PostgresNotificationTokenRepository
import org.tcl.app.repositories.postgres.PostgresNotificationRepository
import org.tcl.app.repositories.postgres.PostgresRefreshTokenRepository
import org.tcl.app.repositories.postgres.PostgresUserRepository
import org.tcl.app.repositories.RefreshTokenRepository
import org.tcl.app.repositories.UserRepository
import org.tcl.app.services.BookingService
import org.tcl.app.services.BookingWebSocketService
import org.tcl.app.services.CourtService
import org.tcl.app.services.FirebaseService
import org.tcl.app.services.NotificationService
import org.tcl.app.services.SlotService
import org.tcl.app.services.UserService

val appModule = module {
    val testing = false

    single<UserRepository> {
        if (testing) FakeUserRepository()
        else PostgresUserRepository()
    }
    single<BookingRepository> {
        if (testing) FakeBookingRepository(get())
        else PostgresBookingRepository()
    }
    single<RefreshTokenRepository> {
        if (testing) FakeRefreshTokenRepository()
        else PostgresRefreshTokenRepository()
    }
    single<NotificationTokenRepository> {
        if (testing) FakeNotificationTokenRepository()
        else PostgresNotificationTokenRepository()
    }
    single<CourtRepository> {
        if (testing) FakeCourtRepository()
        else PostgresCourtRepository()
    }
    single<NotificationRepository> {
        if (testing) FakeNotificationRepository(get())
        else PostgresNotificationRepository()
    }

    singleOf(::UserService)
    singleOf(::BookingWebSocketService)
    singleOf(::BookingService)
    singleOf(::SlotService)
    singleOf(::CourtService)
    singleOf(::FirebaseService)
    singleOf(::NotificationService)
}
