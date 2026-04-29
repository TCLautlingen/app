package org.tcl.app.mappers

import org.tcl.app.booking.Booking
import org.tcl.app.db.entities.BookingEntity

fun entityToBooking(entity: BookingEntity): Booking = Booking(
    id = entity.id.value,
    user = entityToUser(entity.user),
    courtId = entity.court.id.value,
    date = entity.date,
    startTime = entity.startTime,
    duration = entity.duration,
    players = entity.players.map(::entityToUser),
)