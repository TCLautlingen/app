package org.tcl.app.routes

import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.authenticate
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
import org.tcl.app.booking.BookingRequest
import org.tcl.app.booking.VALID_BOOKING_DURATIONS
import org.tcl.app.plugins.JwtConfig.userId
import org.tcl.app.services.BookingService
import org.tcl.app.services.END_TIME
import org.tcl.app.util.plusMinutes

fun Route.bookingRoutes() {
    val bookingService by inject<BookingService>()

    authenticate("auth-jwt") {
        route("/bookings") {
            get("/upcoming") {
                val userId = call.userId()
                val from = requireNotNull(call.queryParameters["from"]) { "Missing 'from' query parameter" }

                val bookings = bookingService.getUpcomingBookingsForUser(
                    userId = userId,
                    from = LocalDate.parse(from)
                )

                call.respond(bookings)
            }

            post {
                val userId = call.userId()
                val req = call.receive<BookingRequest>()
                require(req.duration in VALID_BOOKING_DURATIONS) { "duration must be one of $VALID_BOOKING_DURATIONS" }
                require(userId !in req.playerIds) { "Creator cannot be added as a player" }
                val startTime = LocalTime.parse(req.startTime)
                require(startTime.plusMinutes(req.duration) <= END_TIME) { "Booking extends past closing time (${END_TIME})" }

                val booking = bookingService.createBooking(
                    userId = userId,
                    courtId = req.courtId,
                    date = LocalDate.parse(req.date),
                    startTime = startTime,
                    duration = req.duration,
                    playerIds = req.playerIds,
                ) ?: return@post call.respond(HttpStatusCode.BadRequest, "Could not create booking")

                call.respond(booking)
            }

            delete("/{bookingId}") {
                val userId = call.userId()
                val bookingId = requireNotNull(call.parameters["bookingId"]?.toIntOrNull()) { "Invalid bookingId" }

                val success = bookingService.removeBooking(userId = userId, id = bookingId)
                if (success) {
                    call.respond(HttpStatusCode.OK)
                } else {
                    call.respond(HttpStatusCode.BadRequest, "Could not remove booking")
                }
            }
        }
    }
}
