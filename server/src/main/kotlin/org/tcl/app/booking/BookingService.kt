package org.tcl.app.booking

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import org.tcl.app.Booking
import org.tcl.app.court.CourtRepository
import org.tcl.app.slot.END_TIME
import org.tcl.app.slot.overlapsWith
import org.tcl.app.slot.plusMinutes

class BookingService(
    private val bookingRepository: BookingRepository,
    private val courtRepository: CourtRepository,
) {
    suspend fun getAllBookingsForUser(userId: Int): List<Booking> {
        return bookingRepository.allBookingsForUser(userId)
    }

    suspend fun createBooking(userId: Int, courtId: Int, date: LocalDate, startTime: LocalTime, duration: Int): Booking? {
        courtRepository.courtById(courtId) ?: return null

        if (startTime.plusMinutes(duration) > END_TIME) return null
        val existingBookings = bookingRepository.allBookingsForCourtAndDate(courtId, date)
        val requestedEndTime = startTime.plusMinutes(duration)
        val overlaps = existingBookings.any { booking ->
            booking.overlapsWith(startTime, requestedEndTime)
        }
        if (overlaps) {
            return null
        }

        return bookingRepository.createBooking(userId, courtId, date, startTime, duration)
    }

    suspend fun removeBooking(userId: Int, id: Int): Boolean {
        return bookingRepository.removeBooking(userId, id)
    }
}