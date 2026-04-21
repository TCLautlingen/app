package org.tcl.app.booking

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime

class FakeBookingRepository() : BookingRepository {
    private val bookings = mutableListOf<Booking>()

    private var nextId = 1

    override suspend fun allBookingsForUser(userId: Int): List<Booking> {
        return bookings.filter { it.userId == userId }
    }

    override suspend fun allBookingsForDate(date: LocalDate): List<Booking> {
        return bookings.filter { it.date == date }
    }

    override suspend fun allBookingsForCourtAndDate(courtId: Int, date: LocalDate): List<Booking> {
        return bookings.filter { it.courtId == courtId && it.date == date }
    }

    override suspend fun createBooking(
        userId: Int,
        courtId: Int,
        date: LocalDate,
        startTime: LocalTime,
        duration: Int
    ): Booking {
        val booking = Booking(nextId++, userId, courtId, date, startTime, duration)
        bookings.add(booking)
        return booking
    }

    override suspend fun removeBooking(userId: Int, id: Int): Boolean {
        return bookings.removeIf { it.id == id && it.userId == userId }
    }
}