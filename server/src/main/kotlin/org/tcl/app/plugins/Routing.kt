package org.tcl.app.plugins

import io.ktor.server.application.Application
import io.ktor.server.routing.routing
import org.tcl.app.auth.FakeRefreshTokenRepository
import org.tcl.app.auth.PostgresRefreshTokenRepository
import org.tcl.app.booking.FakeBookingRepository
import org.tcl.app.user.FakeUserRepository
import org.tcl.app.auth.authRoutes
import org.tcl.app.booking.BookingService
import org.tcl.app.booking.PostgresBookingRepository
import org.tcl.app.booking.bookingRoutes
import org.tcl.app.court.CourtService
import org.tcl.app.court.FakeCourtRepository
import org.tcl.app.court.PostgresCourtRepository
import org.tcl.app.court.courtRoutes
import org.tcl.app.device.PostgresDeviceRepository
import org.tcl.app.firebase.FirebaseService
import org.tcl.app.notification.NotificationService
import org.tcl.app.notification.PostgresNotificationRepository
import org.tcl.app.notification.notificationRoutes
import org.tcl.app.slot.SlotService
import org.tcl.app.slot.slotRoutes
import org.tcl.app.user.PostgresUserRepository
import org.tcl.app.user.UserService
import org.tcl.app.user.userRoutes

fun Application.configureRouting() {
    routing {
        authRoutes()
        userRoutes()
        bookingRoutes()
        slotRoutes()
        courtRoutes()
        notificationRoutes()
    }
}