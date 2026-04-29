package org.tcl.app.routes

import io.ktor.server.application.Application
import io.ktor.server.routing.route
import io.ktor.server.routing.routing

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
