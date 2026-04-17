package org.tcl.app.booking.data

import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.HttpStatusCode
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import org.tcl.app.AvailableSlot
import org.tcl.app.Booking
import org.tcl.app.BookingRequest
import org.tcl.app.CourtSlot
import org.tcl.app.core.data.ApiClient

class BookingApiService(
    private val apiClient: ApiClient
) {
    suspend fun getBookings(): List<Booking> {
        return apiClient.client.get("/bookings").body()
    }

    suspend fun createBooking(courtId: Int, date: LocalDate, startTime: LocalTime, duration: Int): Booking {
        val bookingRequest = BookingRequest(
            courtId = courtId,
            date = date.toString(),
            startTime = startTime.toString(),
            duration = duration
        )

        return apiClient.client.post("/bookings") {
            setBody(bookingRequest)
        }.body()
    }

    suspend fun deleteBooking(id: String): Boolean {
        val response = apiClient.client.delete("/bookings/$id")
        return HttpStatusCode.NoContent == response.status
    }

    suspend fun getCourtSlots(courtId: Int, date: String): List<CourtSlot> {
        return apiClient.client.get("/slots/court/$courtId") {
            parameter("date", date)
        }.body()
    }

    suspend fun getAvailableSlots(date: String, duration: Int): List<AvailableSlot> {
        return apiClient.client.get("/slots/available") {
            parameter("date", date)
            parameter("duration", duration)
        }.body()
    }
}
