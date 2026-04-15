package org.tcl.app.routes

import io.ktor.http.HttpStatusCode
import io.ktor.serialization.JsonConvertException
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
import org.tcl.app.AvailableSlot
import org.tcl.app.Booking
import org.tcl.app.COURT_COUNT
import org.tcl.app.CourtSlot
import org.tcl.app.model.BookingRepository

fun Route.bookingRoutes(
    repository: BookingRepository
) {
    authenticate("auth-jwt") {
        route("/bookings") {
            get {
                val principal = call.principal<JWTPrincipal>()
                val userId = principal?.payload?.getClaim("userId")?.asString()

                if (userId == null) {
                    return@get call.respond(HttpStatusCode.Unauthorized)
                }

                val bookings = repository.getBookings()
                    .filter { it.userId == userId }

                call.respond(bookings)
            }

            post {
                val principal = call.principal<JWTPrincipal>()
                val userId = principal?.payload?.getClaim("userId")?.asString()

                if (userId == null) {
                    return@post call.respond(HttpStatusCode.Unauthorized)
                }

                try {
                    val booking = call.receive<Booking>().copy(
                        id = (0..1000000).random().toString(),
                        userId = userId
                    )
                    repository.addBooking(booking)
                    call.respond(booking)
                } catch (ex: IllegalStateException) {
                    call.respond(HttpStatusCode.BadRequest)
                } catch (ex: JsonConvertException) {
                    call.respond(HttpStatusCode.BadRequest)
                }
            }

            get("/courtSlots") {
                val date = call.request.queryParameters["date"]
                    ?: return@get call.respond(HttpStatusCode.BadRequest, "Missing date")

                val court = call.request.queryParameters["court"]?.toIntOrNull()
                    ?: return@get call.respond(HttpStatusCode.BadRequest, "Missing or invalid court")

                val bookings = repository.getBookings()
                    .filter { it.date == date && it.court == court }

                val allStartTimes = generateTimeSlots()
                val slotDuration = 30

                val courtSlots = allStartTimes.map { startTime ->

                    val slotStart = parseMinutes(startTime)
                    val slotEnd = slotStart + slotDuration

                    val taken = bookings.any { booking ->
                        val bookingStart = parseMinutes(booking.startTime)
                        val bookingEnd = bookingStart + booking.duration
                        slotStart < bookingEnd && slotEnd > bookingStart
                    }

                    CourtSlot(
                        startTime = startTime,
                        taken = taken
                    )
                }

                call.respond(courtSlots)
            }

            get("/availableSlots") {
                val date = call.request.queryParameters["date"]
                    ?: return@get call.respond(HttpStatusCode.BadRequest, "Missing date")
                val duration = call.request.queryParameters["duration"]?.toIntOrNull()
                    ?: return@get call.respond(HttpStatusCode.BadRequest, "Missing or invalid duration")

                val bookings = repository.getBookings()
                    .filter { it.date == date }

                val allStartTimes = generateTimeSlots()
                val courts = (1..COURT_COUNT)

                val available = mutableListOf<AvailableSlot>()

                for (startTime in allStartTimes) {
                    for (court in courts) {
                        val requestedStart = parseMinutes(startTime)
                        val requestedEnd = requestedStart + duration

                        val hasConflict = bookings.any { booking ->
                            if (booking.court != court) return@any false
                            val existingStart = parseMinutes(booking.startTime)
                            val existingEnd = existingStart + booking.duration
                            requestedStart < existingEnd && requestedEnd > existingStart
                        }

                        if (!hasConflict) {
                            available.add(AvailableSlot(startTime, court))
                        }
                    }
                }

                call.respond(available)
            }

            delete("/{bookingId}") {
                val principal = call.principal<JWTPrincipal>()
                val userId = principal?.payload?.getClaim("userId")?.asString()

                if (userId == null) {
                    return@delete call.respond(HttpStatusCode.Unauthorized)
                }

                val bookingId = call.parameters["bookingId"]
                if (bookingId == null) {
                    call.respond(HttpStatusCode.BadRequest)
                    return@delete
                }
                if (repository.deleteBooking(userId, bookingId)) {
                    call.respond(HttpStatusCode.NoContent)
                } else {
                    call.respond(HttpStatusCode.NotFound)
                }
            }
        }
    }
}

fun parseMinutes(time: String): Int {
    val (h, m) = time.split(":").map { it.toInt() }
    return h * 60 + m
}

fun generateTimeSlots(): List<String> {
    return (8 * 60 until 22 * 60 step 30).map { minutes ->
        "%02d:%02d".format(minutes / 60, minutes % 60)
    }
}