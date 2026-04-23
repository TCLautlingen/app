package org.tcl.app.repositories

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.core.greaterEq
import org.jetbrains.exposed.v1.core.inSubQuery
import org.jetbrains.exposed.v1.core.or
import org.jetbrains.exposed.v1.jdbc.SizedCollection
import org.jetbrains.exposed.v1.jdbc.select
import org.tcl.app.booking.Booking
import org.tcl.app.entities.BookingEntity
import org.tcl.app.entities.CourtEntity
import org.tcl.app.entities.UserEntity
import org.tcl.app.mappers.entityToBooking
import org.tcl.app.plugins.withTransaction
import org.tcl.app.tables.BookingPlayersTable
import org.tcl.app.tables.BookingsTable

class PostgresBookingRepository : BookingRepository {
    override suspend fun allBookingsForUser(userId: Int): List<Booking> = withTransaction {
        BookingEntity
            .find { BookingsTable.user eq userId }
            .map(::entityToBooking)
    }

    override suspend fun upcomingBookingsForUser(userId: Int, from: LocalDate): List<Booking> = withTransaction {
        BookingEntity.find {
            (BookingsTable.date greaterEq from) and
                    (BookingsTable.user eq userId or (BookingsTable.id inSubQuery
                            BookingPlayersTable
                                .select(BookingPlayersTable.booking)
                                .where { BookingPlayersTable.user eq userId }
                            ))
        }
            .map(::entityToBooking)
    }

    override suspend fun allBookingsForDate(date: LocalDate): List<Booking> = withTransaction {
        BookingEntity
            .find { BookingsTable.date eq date }
            .map(::entityToBooking)
    }

    override suspend fun allBookingsForCourtAndDate(courtId: Int, date: LocalDate): List<Booking> = withTransaction {
        BookingEntity
            .find { (BookingsTable.court eq courtId) and (BookingsTable.date eq date) }
            .map(::entityToBooking)
    }

    override suspend fun createBooking(
        userId: Int,
        courtId: Int,
        date: LocalDate,
        startTime: LocalTime,
        duration: Int,
        playerIds: List<Int>
    ): Booking = withTransaction {
        BookingEntity.new {
            this.user = UserEntity[userId]
            this.court = CourtEntity[courtId]
            this.date = date
            this.startTime = startTime
            this.duration = duration
            this.players = SizedCollection(playerIds.map { UserEntity[it] })
        }.let(::entityToBooking)
    }

    override suspend fun removeBooking(userId: Int, id: Int): Boolean = withTransaction {
        BookingEntity
            .find { (BookingsTable.id eq id) and (BookingsTable.user eq userId) }
            .limit(1)
            .firstOrNull()
            ?.delete() != null
    }
}
