package org.tcl.app.booking.data

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType.Application.Json
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.tcl.app.AvailabilityRequest
import org.tcl.app.AvailableSlot
import org.tcl.app.Booking
import org.tcl.app.SERVER_PORT

class BookingApiService(
    baseUrl: String = "http://localhost:$SERVER_PORT"
) {
    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
        defaultRequest {
            url(baseUrl)
            contentType(Json)
        }
    }

    suspend fun getBookings(): List<Booking> {
        return client.get("/bookings").body()
    }

    suspend fun addBooking(booking: Booking): Booking {
        return client.post("/bookings") {
            setBody(booking)
        }.body()
    }

    suspend fun deleteBooking(id: String): Boolean {
        val response = client.delete("/bookings/$id")
        return HttpStatusCode.NoContent == response.status
    }

    suspend fun getAvailability(date: String, duration: Int): List<AvailableSlot> {
        return client.post("/bookings/availability") {
            setBody(AvailabilityRequest(date, duration))
        }.body()
    }
}
