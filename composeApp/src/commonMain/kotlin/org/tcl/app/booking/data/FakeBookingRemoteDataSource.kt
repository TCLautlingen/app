package org.tcl.app.booking.data

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import org.tcl.app.booking.AvailableSlot
import org.tcl.app.booking.Booking
import org.tcl.app.court.Court
import org.tcl.app.booking.CourtSlot
import org.tcl.app.booking.domain.BookingRemoteDataSource
import org.tcl.app.core.domain.util.DataError
import org.tcl.app.core.domain.util.EmptyResult
import org.tcl.app.core.domain.util.Result
import org.tcl.app.user.User

class FakeBookingRemoteDataSource : BookingRemoteDataSource {
    private val bookings = mutableListOf(
        Booking(id = 1, user = User(1, "", "", ""), courtId = 1, date = LocalDate(2026, 4, 21), startTime = LocalTime(10, 0), duration = 60, players = emptyList()),
        Booking(id = 2, user = User(1, "", "", ""), courtId = 2, date = LocalDate(2026, 4, 22), startTime = LocalTime(14, 0), duration = 90, players = emptyList()),
    )

    override suspend fun getUpcomingBookings(from: String): Result<List<Booking>, DataError> =
        Result.Success(bookings)

    override suspend fun createBooking(
        courtId: Int,
        date: LocalDate,
        startTime: LocalTime,
        duration: Int,
        playerIds: List<Int>
    ): Result<Booking, DataError> {
        val new = Booking(
            id = bookings.size + 1,
            user = User(1, "", "", ""),
            courtId = courtId,
            date = date,
            startTime = startTime,
            duration = duration,
            players = emptyList(),
        )
        bookings.add(new)
        return Result.Success(new)
    }

    override suspend fun deleteBooking(id: Int): EmptyResult<DataError> {
        bookings.removeAll { it.id == id }
        return Result.Success(Unit)
    }

    override suspend fun getCourtSlots(
        courtId: Int,
        date: String
    ): Result<List<CourtSlot>, DataError> = Result.Success(
        listOf(
            CourtSlot(startTime = "09:00", taken = true),
            CourtSlot(startTime = "10:00", taken = false),
            CourtSlot(startTime = "11:00", taken = false),
            CourtSlot(startTime = "12:00", taken = true),
        )
    )

    override suspend fun getAvailableSlots(
        date: String,
        duration: Int
    ): Result<List<AvailableSlot>, DataError> = Result.Success(
        listOf(
            AvailableSlot(startTime = "10:00", court = Court(id = 1, name = "Court 1")),
            AvailableSlot(startTime = "11:00", court = Court(id = 2, name = "Court 2")),
            AvailableSlot(startTime = "14:00", court = Court(id = 1, name = "Court 1")),
        )
    )
}