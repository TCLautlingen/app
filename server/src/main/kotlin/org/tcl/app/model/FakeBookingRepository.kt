package org.tcl.app.model

import org.tcl.app.Booking

class FakeBookingRepository : BookingRepository {
    private val bookings = mutableListOf<Booking>()

    override fun getBookings(): List<Booking> {
        return bookings
    }

    override fun addBooking(booking: Booking) {
        bookings.add(booking)
    }

    override fun deleteBooking(userId: String, id: String): Boolean {
        return bookings.removeIf { it.userId == userId && it.id == id  }
    }
}