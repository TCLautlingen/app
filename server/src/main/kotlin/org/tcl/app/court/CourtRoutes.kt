package org.tcl.app.court

import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import org.koin.ktor.ext.inject

fun Route.courtRoutes() {
    val courtService by inject<CourtService>()

    route("/courts") {
        get {
            val courts = courtService.getAllCourts()
            call.respond(courts)
        }
    }
}