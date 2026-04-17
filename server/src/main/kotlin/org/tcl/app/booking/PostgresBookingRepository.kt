package org.tcl.app.booking

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.eq
import org.tcl.app.Booking
import org.tcl.app.plugins.withTransaction

class PostgresBookingRepository() : BookingRepository {
    override suspend fun allBookingsForUser(userId: Int): List<Booking> = withTransaction {
        BookingDAO
            .find { BookingTable.userId eq userId }
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
        duration: Int
    ): Booking = withTransaction {
        BookingDAO.new {
            this.userId = userId
            this.courtId = courtId
            this.date = date
            this.startTime = startTime
            this.duration = duration
        }.let(::daoToBooking)
    }
    override suspend fun removeBooking(userId: Int, id: Int): Boolean = withTransaction {
        BookingDAO
            .find { (BookingTable.id eq id) and (BookingTable.userId eq userId) }
            .limit(1)
            .firstOrNull()
            ?.delete() != null
    }

}