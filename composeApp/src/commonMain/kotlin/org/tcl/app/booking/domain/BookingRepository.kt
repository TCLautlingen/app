package org.tcl.app.booking.domain

import org.tcl.app.AvailableSlot
import org.tcl.app.Booking
import org.tcl.app.CourtSlot
import org.tcl.app.booking.data.BookingApiService

class BookingRepository(
    private val api: BookingApiService
) {
    suspend fun getBookings(): List<Booking> =
        api.getBookings()

    suspend fun addBooking(booking: Booking): Booking =
        api.addBooking(booking)

    suspend fun getCourtSlots(date: String, court: Int): List<CourtSlot> =
        api.getCourtSlots(date, court)

    suspend fun getAvailableSlots(date: String, duration: Int): List<AvailableSlot> =
        api.getAvailableSlots(date, duration)

    suspend fun deleteBooking(id: String): Boolean =
        api.deleteBooking(id)

}