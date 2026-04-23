package org.tcl.app.mappers

import org.tcl.app.booking.Booking
import org.tcl.app.entities.BookingEntity

fun entityToBooking(entity: BookingEntity): Booking = Booking(
    id = entity.id.value,
    userId = entity.user.id.value,
    courtId = entity.court.id.value,
    date = entity.date,
    startTime = entity.startTime,
    duration = entity.duration,
    players = entity.players.map(::entityToUser),
)