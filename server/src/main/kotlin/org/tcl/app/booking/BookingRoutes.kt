package org.tcl.app.booking

import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import org.koin.ktor.ext.inject
import org.tcl.app.security.JwtConfig.userId

fun Route.bookingRoutes() {
    val bookingService by inject<BookingService>()

    authenticate("auth-jwt") {
        route("/bookings") {
            get("/upcoming") {
                val userId = call.userId()
                val date = call.queryParameters["from"]
                    ?: return@get call.respond(HttpStatusCode.BadRequest, "Missing date query parameter")

                val bookings = bookingService.getUpcomingBookingsForUser(
                    userId = userId,
                    from = LocalDate.parse(date)
                )

                call.respond(bookings)
            }

            post {
                val bookingRequest = call.receive<BookingRequest>()
                val userId = call.userId()

                val booking = bookingService.createBooking(
                    userId = userId,
                    courtId = bookingRequest.courtId,
                    date = LocalDate.parse(bookingRequest.date),
                    startTime = LocalTime.parse(bookingRequest.startTime),
                    duration = bookingRequest.duration,
                    playerIds = bookingRequest.playerIds,

                ) ?: return@post call.respond(HttpStatusCode.BadRequest, "Could not create booking")

                call.respond(booking)
            }

            delete("/{bookingId}") {
                val userId = call.userId()
                val bookingId = call.parameters["bookingId"]?.toIntOrNull()
                    ?: return@delete call.respond(HttpStatusCode.BadRequest)

                val success = bookingService.removeBooking(
                    userId = userId,
                    id = bookingId
                )
                if (success) {
                    call.respond(HttpStatusCode.OK)
                } else {
                    call.respond(HttpStatusCode.BadRequest, "Could not remove booking")
                }
            }
        }
    }
}