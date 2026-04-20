package org.tcl.app.booking.data

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import org.tcl.app.AvailableSlot
import org.tcl.app.Booking
import org.tcl.app.CourtSlot
import org.tcl.app.booking.domain.BookingRepository
import org.tcl.app.core.domain.util.DataError
import org.tcl.app.core.domain.util.EmptyResult
import org.tcl.app.core.domain.util.Result

class BookingRepositoryImpl(
    private val api: BookingApiService
) : BookingRepository {
    override suspend fun getBookings(): Result<List<Booking>, DataError> =
        api.getBookings()

    override suspend fun createBooking(courtId: Int, date: LocalDate, startTime: LocalTime, duration: Int): Result<Booking, DataError> =
        api.createBooking(courtId, date, startTime, duration)

    override suspend fun deleteBooking(id: String): EmptyResult<DataError> =
        api.deleteBooking(id)

    override suspend fun getCourtSlots(courtId: Int, date: String): Result<List<CourtSlot>, DataError> =
        api.getCourtSlots(courtId, date)

    override suspend fun getAvailableSlots(date: String, duration: Int): Result<List<AvailableSlot>, DataError> =
        api.getAvailableSlots(date, duration)

}