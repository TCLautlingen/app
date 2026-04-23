package org.tcl.app.routes

import io.ktor.server.auth.authenticate
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import kotlinx.datetime.LocalDate
import org.koin.ktor.ext.inject
import org.tcl.app.services.SlotService

fun Route.slotRoutes() {
    val slotService by inject<SlotService>()

    authenticate("auth-jwt") {
        route("/slots") {
            get("court/{courtId}") {
                val courtId = requireNotNull(call.parameters["courtId"]?.toIntOrNull()) { "Invalid courtId" }
                val date = requireNotNull(call.queryParameters["date"]) { "Missing 'date' query parameter" }

                val courtSlots = slotService.getCourtSlots(
                    courtId = courtId,
                    date = LocalDate.parse(date)
                )
                call.respond(courtSlots)
            }

            get("/available") {
                val date = requireNotNull(call.queryParameters["date"]) { "Missing 'date' query parameter" }
                val duration = requireNotNull(call.queryParameters["duration"]?.toIntOrNull()) { "Missing or invalid 'duration' query parameter" }

                val availableSlots = slotService.getAvailableSlots(
                    date = LocalDate.parse(date),
                    duration = duration
                )
                call.respond(availableSlots)
            }
        }
    }
}
