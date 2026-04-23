package org.tcl.app.plugins

import io.ktor.server.application.Application
import io.ktor.server.routing.routing
import org.tcl.app.routes.authRoutes
import org.tcl.app.routes.bookingRoutes
import org.tcl.app.routes.courtRoutes
import org.tcl.app.routes.notificationRoutes
import org.tcl.app.routes.slotRoutes
import org.tcl.app.routes.userRoutes

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
