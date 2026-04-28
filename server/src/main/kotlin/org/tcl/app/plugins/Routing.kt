package org.tcl.app.plugins

import io.ktor.server.application.Application
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import org.tcl.app.routes.authRoutes
import org.tcl.app.routes.bookingRoutes
import org.tcl.app.routes.courtRoutes
import org.tcl.app.routes.healthRoutes
import org.tcl.app.routes.notificationRoutes
import org.tcl.app.routes.slotRoutes
import org.tcl.app.routes.userRoutes

fun Application.configureRouting() {
    routing {
        route("/v1") {
            healthRoutes()
            authRoutes()
            userRoutes()
            bookingRoutes()
            slotRoutes()
            courtRoutes()
            notificationRoutes()
        }
    }
}
