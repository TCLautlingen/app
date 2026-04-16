package org.tcl.app.booking.data

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
import org.tcl.app.core.data.ApiClient

class BookingApiService(
    private val apiClient: ApiClient
) {
    suspend fun getBookings(): List<Booking> {
        return apiClient.client.get("/bookings").body()
    }

    suspend fun addBooking(booking: Booking): Booking {
        return apiClient.client.post("/bookings") {
            setBody(booking)
        }.body()
    }

    suspend fun getCourtSlots(date: String, court: Int): List<CourtSlot> {
        return apiClient.client.get("/bookings/courtSlots") {
            parameter("date", date)
            parameter("court", court)
        }.body()
    }

    suspend fun getAvailableSlots(date: String, duration: Int): List<AvailableSlot> {
        return apiClient.client.get("/bookings/availableSlots") {
            parameter("date", date)
            parameter("duration", duration)
        }.body()
    }

    suspend fun deleteBooking(id: String): Boolean {
        val response = apiClient.client.delete("/bookings/$id")
        return HttpStatusCode.NoContent == response.status
    }
}
