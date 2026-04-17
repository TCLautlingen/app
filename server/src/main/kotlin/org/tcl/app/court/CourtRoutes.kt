package org.tcl.app.court

import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route

fun Route.courtRoutes(
    courtService: CourtService
) {
    route("/courts") {
        get {
            val courts = courtService.getAllCourts()
            call.respond(courts)
        }
    }
}