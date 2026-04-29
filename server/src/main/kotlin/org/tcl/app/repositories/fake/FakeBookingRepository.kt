package org.tcl.app.repositories.fake

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import org.tcl.app.booking.Booking
import org.tcl.app.repositories.BookingRepository
import org.tcl.app.repositories.UserRepository

class FakeBookingRepository(
    private val userRepository: UserRepository
) : BookingRepository {
    private val bookings = mutableListOf<Booking>()
    private var nextId = 1

    override suspend fun allBookingsForUser(userId: Int): List<Booking> {
        return bookings.filter { it.user.id == userId }
    }

    override suspend fun upcomingBookingsForUser(userId: Int, from: LocalDate): List<Booking> {
        return bookings.filter { booking ->
            booking.date >= from &&
                    (booking.user.id == userId || booking.players.any { it.id == userId })
        }
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
        duration: Int,
        playerIds: List<Int>
    ): Booking {
        val players = playerIds.mapNotNull { playerId -> userRepository.userById(playerId) }
        val booking = Booking(nextId++, userRepository.userById(userId)!!, courtId, date, startTime, duration, players)
        bookings.add(booking)
        return booking
    }

    override suspend fun removeBooking(userId: Int, id: Int): Boolean {
        return bookings.removeIf { it.id == id && it.user.id == userId }
    }
}
