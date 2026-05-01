package org.tcl.app.repositories

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import org.tcl.app.booking.Booking

interface BookingRepository {
    suspend fun allBookingsForUser(userId: Int): List<Booking>
    suspend fun upcomingBookingsForUser(userId: Int, from: LocalDate): List<Booking>
    suspend fun allBookingsForDate(date: LocalDate): List<Booking>
    suspend fun allBookingsForCourtAndDate(courtId: Int, date: LocalDate): List<Booking>
    suspend fun createBooking(
        userId: Int,
        courtId: Int,
        date: LocalDate,
        startTime: LocalTime,
        duration: Int,
        playerIds: List<Int>
    ): Booking
    suspend fun bookingById(id: Int): Booking?
    suspend fun removeBooking(userId: Int, id: Int): Boolean
}
