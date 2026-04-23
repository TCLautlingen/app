package org.tcl.app.repositories

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.core.greaterEq
import org.jetbrains.exposed.v1.core.inSubQuery
import org.jetbrains.exposed.v1.core.or
import org.jetbrains.exposed.v1.jdbc.batchInsert
import org.jetbrains.exposed.v1.jdbc.select
import org.tcl.app.booking.Booking
import org.tcl.app.models.BookingDAO
import org.tcl.app.models.BookingPlayerTable
import org.tcl.app.models.BookingTable
import org.tcl.app.models.daoToBooking
import org.tcl.app.plugins.withTransaction

class PostgresBookingRepository : BookingRepository {
    override suspend fun allBookingsForUser(userId: Int): List<Booking> = withTransaction {
        BookingDAO
            .find { BookingTable.userId eq userId }
            .map(::daoToBooking)
    }

    override suspend fun upcomingBookingsForUser(userId: Int, from: LocalDate): List<Booking> = withTransaction {
        BookingDAO.find {
            (BookingTable.date greaterEq from) and
                    (BookingTable.userId eq userId or (BookingTable.id inSubQuery
                            BookingPlayerTable
                                .select(BookingPlayerTable.bookingId)
                                .where { BookingPlayerTable.userId eq userId }
                            ))
        }
            .map(::daoToBooking)
    }

    override suspend fun allBookingsForDate(date: LocalDate): List<Booking> = withTransaction {
        BookingDAO
            .find { BookingTable.date eq date }
            .map(::daoToBooking)
    }

    override suspend fun allBookingsForCourtAndDate(courtId: Int, date: LocalDate): List<Booking> = withTransaction {
        BookingDAO
            .find { (BookingTable.courtId eq courtId) and (BookingTable.date eq date) }
            .map(::daoToBooking)
    }

    override suspend fun createBooking(
        userId: Int,
        courtId: Int,
        date: LocalDate,
        startTime: LocalTime,
        duration: Int,
        playerIds: List<Int>
    ): Booking = withTransaction {
        val booking = BookingDAO.new {
            this.userId = userId
            this.courtId = courtId
            this.date = date
            this.startTime = startTime
            this.duration = duration
        }

        BookingPlayerTable.batchInsert(playerIds) { playerId ->
            this[BookingPlayerTable.bookingId] = booking.id.value
            this[BookingPlayerTable.userId] = playerId
        }

        daoToBooking(booking)
    }

    override suspend fun removeBooking(userId: Int, id: Int): Boolean = withTransaction {
        BookingDAO
            .find { (BookingTable.id eq id) and (BookingTable.userId eq userId) }
            .limit(1)
            .firstOrNull()
            ?.delete() != null
    }
}
