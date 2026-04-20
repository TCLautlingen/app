package org.tcl.app.booking.domain

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import org.tcl.app.AvailableSlot
import org.tcl.app.Booking
import org.tcl.app.CourtSlot
import org.tcl.app.core.domain.util.DataError
import org.tcl.app.core.domain.util.EmptyResult
import org.tcl.app.core.domain.util.Result

interface BookingRepository {
    suspend fun getBookings(): Result<List<Booking>, DataError>

    suspend fun createBooking(courtId: Int, date: LocalDate, startTime: LocalTime, duration: Int): Result<Booking, DataError>

    suspend fun deleteBooking(id: String): EmptyResult<DataError>

    suspend fun getCourtSlots(courtId: Int, date: String): Result<List<CourtSlot>, DataError>

    suspend fun getAvailableSlots(date: String, duration: Int): Result<List<AvailableSlot>, DataError>

}