package org.tcl.app.services

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import org.tcl.app.booking.Booking
import org.tcl.app.booking.BookingWsMessage
import org.tcl.app.repositories.BookingRepository
import org.tcl.app.repositories.CourtRepository
import org.tcl.app.repositories.NotificationTokenRepository
import org.tcl.app.repositories.UserRepository
import org.tcl.app.util.formatDdMmYyyy
import org.tcl.app.util.plusMinutes

class BookingService(
    private val bookingRepository: BookingRepository,
    private val courtRepository: CourtRepository,
    private val firebaseService: FirebaseService,
    private val notificationTokenRepository: NotificationTokenRepository,
    private val userRepository: UserRepository,
    private val bookingWebSocketService: BookingWebSocketService,
) {
    suspend fun getAllBookingsForUser(userId: Int): List<Booking> {
        return bookingRepository.allBookingsForUser(userId)
    }

    suspend fun getUpcomingBookingsForUser(userId: Int, from: LocalDate): List<Booking> {
        return bookingRepository.upcomingBookingsForUser(userId, from)
    }

    suspend fun createBooking(
        userId: Int,
        courtId: Int,
        date: LocalDate,
        startTime: LocalTime,
        duration: Int,
        playerIds: List<Int>
    ): Booking? {
        courtRepository.courtById(courtId) ?: return null

        val creator = userRepository.userById(userId)
            ?: return null

        val existingBookings = bookingRepository.allBookingsForCourtAndDate(courtId, date)
        val requestedEndTime = startTime.plusMinutes(duration)
        val overlaps = existingBookings.any { booking ->
            booking.overlapsWith(startTime, requestedEndTime)
        }
        if (overlaps) {
            return null
        }

        val notificationTokens = mutableListOf<String>()
        for (playerId in playerIds) {
            notificationTokens.addAll(notificationTokenRepository.getTokensForUser(playerId))
        }
        firebaseService.sendToTokens(
            notificationTokens,
            title = "Tennismatch am ${date.formatDdMmYyyy()} um $startTime",
            body = "Du wurdest von ${creator.firstName} ${creator.lastName} hinzugefügt.",
        )

        val booking = bookingRepository.createBooking(userId, courtId, date, startTime, duration, playerIds)
        val affectedUserIds = (listOf(userId) + playerIds).distinct()
        bookingWebSocketService.notifyUsers(affectedUserIds, BookingWsMessage.BookingCreated(booking))
        return booking
    }

    suspend fun removeBooking(userId: Int, id: Int): Boolean {
        val booking = bookingRepository.bookingById(id)
        val success = bookingRepository.removeBooking(userId, id)
        if (success && booking != null) {
            val affectedUserIds = (listOf(booking.user.id) + booking.players.map { it.id }).distinct()
            bookingWebSocketService.notifyUsers(affectedUserIds, BookingWsMessage.BookingDeleted(id))
        }
        return success
    }
}
