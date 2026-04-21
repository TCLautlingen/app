package org.tcl.app.slot

import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.authenticate
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import kotlinx.datetime.LocalDate
import org.koin.ktor.ext.inject

fun Route.slotRoutes() {
    val slotService by inject<SlotService>()

    authenticate("auth-jwt") {
        route("/slots") {
            get("court/{courtId}") {
                val courtId = call.parameters["courtId"]?.toIntOrNull()
                    ?: return@get call.respond(HttpStatusCode.BadRequest, "Missing or invalid courtId parameter")

                val date = call.queryParameters["date"]
                    ?: return@get call.respond(HttpStatusCode.BadRequest, "Missing date query parameter")

                val courtSlots = slotService.getCourtSlots(
                    courtId = courtId,
                    date = LocalDate.parse(date)
                )
                call.respond(courtSlots)
            }

            get("/available") {
                val date = call.queryParameters["date"]
                    ?: return@get call.respond(HttpStatusCode.BadRequest, "Missing date query parameter")

                val duration = call.queryParameters["duration"]?.toIntOrNull()
                    ?: return@get call.respond(HttpStatusCode.BadRequest, "Missing or invalid duration query parameter")

                val availableSlots = slotService.getAvailableSlots(
                    date = LocalDate.parse(date),
                    duration = duration
                )
                call.respond(availableSlots)
            }
        }
    }
}