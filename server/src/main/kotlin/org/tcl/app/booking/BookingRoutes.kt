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
import org.tcl.app.security.JwtConfig.toAuthPrincipal

fun Route.bookingRoutes() {
    val bookingService by inject<BookingService>()

    authenticate("auth-jwt") {
        route("/bookings") {
            get {
                val authPrincipal = call.principal<JWTPrincipal>()?.toAuthPrincipal()
                    ?: return@get call.respond(HttpStatusCode.Unauthorized)

                val bookings = bookingService.getAllBookingsForUser(authPrincipal.userId)

                call.respond(bookings)
            }

            post {
                val authPrincipal = call.principal<JWTPrincipal>()?.toAuthPrincipal()
                    ?: return@post call.respond(HttpStatusCode.Unauthorized)

                val bookingRequest = call.receive<BookingRequest>()

                val booking = bookingService.createBooking(
                    userId = authPrincipal.userId,
                    courtId = bookingRequest.courtId,
                    date = LocalDate.parse(bookingRequest.date),
                    startTime = LocalTime.parse(bookingRequest.startTime),
                    duration = bookingRequest.duration,
                ) ?: return@post call.respond(HttpStatusCode.BadRequest, "Could not create booking")

                call.respond(booking)
            }

            delete("/{bookingId}") {
                val authPrincipal = call.principal<JWTPrincipal>()?.toAuthPrincipal()
                    ?: return@delete call.respond(HttpStatusCode.Unauthorized)

                val bookingId = call.parameters["bookingId"]?.toIntOrNull()
                    ?: return@delete call.respond(HttpStatusCode.BadRequest)

                val success = bookingService.removeBooking(
                    userId = authPrincipal.userId,
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