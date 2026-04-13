package org.tcl.app

import io.ktor.http.HttpStatusCode
import io.ktor.serialization.JsonConvertException
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.*
import org.tcl.app.model.BookingRepository

fun Application.configureSerialization(repository: BookingRepository) {
    install(ContentNegotiation) {
        json()
    }

    routing {
        route("/bookings") {
            get {
                val bookings = repository.getBookings()
                call.respond(bookings)
            }

            post {
                try {
                    val booking = call.receive<Booking>().copy(id = (0..1000000).random().toString())
                    repository.addBooking(booking)
                    call.respond(booking)
                } catch (ex: IllegalStateException) {
                    call.respond(HttpStatusCode.BadRequest)
                } catch (ex: JsonConvertException) {
                    call.respond(HttpStatusCode.BadRequest)
                }
            }

            post("/availability") {
                val request = call.receive<AvailabilityRequest>()
                val bookings = repository.getBookings()
                    .filter { it.date == request.date }

                val allStartTimes = generateTimeSlots()
                val courts = (1..COURT_COUNT)

                val available = mutableListOf<AvailableSlot>()

                for (startTime in allStartTimes) {
                    for (court in courts) {
                        val requestedStart = parseMinutes(startTime)
                        val requestedEnd = requestedStart + request.duration

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
                val bookingId = call.parameters["bookingId"]
                if (bookingId == null) {
                    call.respond(HttpStatusCode.BadRequest)
                    return@delete
                }
                if (repository.deleteBooking(bookingId)) {
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