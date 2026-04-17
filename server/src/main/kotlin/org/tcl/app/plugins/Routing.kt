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
import org.tcl.app.slot.SlotService
import org.tcl.app.slot.slotRoutes
import org.tcl.app.user.PostgresUserRepository
import org.tcl.app.user.UserService
import org.tcl.app.user.userRoutes

fun Application.configureRouting() {
    val userRepository = PostgresUserRepository()
    val bookingRepository = PostgresBookingRepository()
    val refreshTokenRepository = PostgresRefreshTokenRepository()
    val userService = UserService(userRepository, refreshTokenRepository)
    val courtRepository = PostgresCourtRepository()
    val bookingService = BookingService(bookingRepository, courtRepository)
    val slotService = SlotService(bookingRepository, courtRepository)
    val courtService = CourtService(courtRepository)

    routing {
        authRoutes(userService)
        userRoutes(userService)
        bookingRoutes(bookingService)
        slotRoutes(slotService)
        courtRoutes(courtService)
    }
}