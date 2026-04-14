package org.tcl.app.plugins

import io.ktor.server.application.Application
import io.ktor.server.routing.routing
import org.tcl.app.model.FakeBookingRepository
import org.tcl.app.routes.authRoutes
import org.tcl.app.routes.bookingRoutes

fun Application.configureRouting() {
    val bookingRepository = FakeBookingRepository()

    routing {
        authRoutes()
        bookingRoutes(bookingRepository)
    }
}