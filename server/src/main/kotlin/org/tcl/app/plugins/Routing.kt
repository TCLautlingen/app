package org.tcl.app.plugins

import io.ktor.server.application.Application
import io.ktor.server.routing.routing
import org.tcl.app.model.FakeBookingRepository
import org.tcl.app.model.FakeUserRepository
import org.tcl.app.routes.authRoutes
import org.tcl.app.routes.bookingRoutes

fun Application.configureRouting() {
    val userRepository = FakeUserRepository()
    val bookingRepository = FakeBookingRepository()

    routing {
        authRoutes(userRepository)
        bookingRoutes(bookingRepository)
    }
}