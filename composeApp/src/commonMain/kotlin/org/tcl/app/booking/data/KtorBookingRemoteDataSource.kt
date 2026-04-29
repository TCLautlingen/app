package org.tcl.app.booking.data

import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import org.tcl.app.booking.AvailableSlot
import org.tcl.app.booking.Booking
import org.tcl.app.booking.BookingRequest
import org.tcl.app.booking.CourtSlot
import org.tcl.app.booking.domain.BookingRemoteDataSource
import org.tcl.app.core.data.network.BackendApiClient
import org.tcl.app.core.domain.util.DataError
import org.tcl.app.core.domain.util.EmptyResult
import org.tcl.app.core.domain.util.Result
import org.tcl.app.core.domain.util.safeApiCall

class KtorBookingRemoteDataSource(
    private val backendApiClient: BackendApiClient
) : BookingRemoteDataSource {
    override suspend fun getUpcomingBookings(from: String): Result<List<Booking>, DataError> = safeApiCall {
            backendApiClient.client.get("bookings/upcoming") {
                parameter("from", from)
            }
    }

    override suspend fun createBooking(
        courtId: Int,
        date: LocalDate,
        startTime: LocalTime,
        duration: Int,
        playerIds: List<Int>,
    ): Result<Booking, DataError> {
        val bookingRequest = BookingRequest(
            courtId = courtId,
            date = date.toString(),
            startTime = startTime.toString(),
            duration = duration,
            playerIds = playerIds
        )

        return safeApiCall {
            backendApiClient.client.post("bookings") {
                setBody(bookingRequest)
            }
        }
    }

    override suspend fun deleteBooking(id: Int): EmptyResult<DataError> = safeApiCall {
        backendApiClient.client.delete("bookings/$id")
    }

    override suspend fun getCourtSlots(courtId: Int, date: String): Result<List<CourtSlot>, DataError> = safeApiCall {
        backendApiClient.client.get("slots/court/$courtId") {
            parameter("date", date)
        }
    }

    override suspend fun getAvailableSlots(date: String, duration: Int): Result<List<AvailableSlot>, DataError> = safeApiCall {
        backendApiClient.client.get("slots/available") {
            parameter("date", date)
            parameter("duration", duration)
        }
    }
}
