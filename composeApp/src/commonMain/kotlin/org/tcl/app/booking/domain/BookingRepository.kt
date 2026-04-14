package org.tcl.app.booking.domain

import org.tcl.app.AvailableSlot
import org.tcl.app.Booking
import org.tcl.app.booking.data.BookingApiService

class BookingRepository(
    private val api: BookingApiService
) {
    suspend fun getBookings(): List<Booking> =
        api.getBookings()

    suspend fun deleteBooking(id: String): Boolean =
        api.deleteBooking(id)

    suspend fun addBooking(booking: Booking): Booking =
        api.addBooking(booking)

    suspend fun getAvailability(date: String, duration: Int): List<AvailableSlot> =
        api.getAvailability(date, duration)
}