package org.tcl.app.booking.data

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.HttpStatusCode
import org.tcl.app.AvailabilityRequest
import org.tcl.app.AvailableSlot
import org.tcl.app.Booking

class BookingApiService(
    private val client: HttpClient
) {

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
