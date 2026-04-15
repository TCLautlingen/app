package org.tcl.app.booking.data

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.HttpStatusCode
import org.tcl.app.AvailableSlot
import org.tcl.app.Booking
import org.tcl.app.CourtSlot

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

    suspend fun getCourtSlots(date: String, court: Int): List<CourtSlot> {
        return client.get("/bookings/courtSlots") {
            parameter("date", date)
            parameter("court", court)
        }.body()
    }

    suspend fun getAvailableSlots(date: String, duration: Int): List<AvailableSlot> {
        return client.get("/bookings/availableSlots") {
            parameter("date", date)
            parameter("duration", duration)
        }.body()
    }

    suspend fun deleteBooking(id: String): Boolean {
        val response = client.delete("/bookings/$id")
        return HttpStatusCode.NoContent == response.status
    }
}
