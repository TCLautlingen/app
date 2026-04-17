package org.tcl.app.booking.domain

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import org.tcl.app.AvailableSlot
import org.tcl.app.Booking
import org.tcl.app.CourtSlot
import org.tcl.app.booking.data.BookingApiService

class BookingRepository(
    private val api: BookingApiService
) {
    suspend fun getBookings(): List<Booking> =
        api.getBookings()

    suspend fun createBooking(courtId: Int, date: LocalDate, startTime: LocalTime, duration: Int): Booking =
        api.createBooking(courtId, date, startTime, duration)

    suspend fun deleteBooking(id: String): Boolean =
        api.deleteBooking(id)

    suspend fun getCourtSlots(courtId: Int, date: String): List<CourtSlot> =
        api.getCourtSlots(courtId, date)

    suspend fun getAvailableSlots(date: String, duration: Int): List<AvailableSlot> =
        api.getAvailableSlots(date, duration)

}