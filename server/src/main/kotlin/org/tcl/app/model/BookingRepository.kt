package org.tcl.app.model

import org.tcl.app.Booking

interface BookingRepository {
    fun getBookings(): List<Booking>
    fun addBooking(booking: Booking)
    fun deleteBooking(id: String): Boolean
}