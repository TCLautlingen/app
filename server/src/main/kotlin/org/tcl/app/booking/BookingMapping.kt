package org.tcl.app.booking

import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.core.dao.id.IntIdTable
import org.jetbrains.exposed.v1.dao.IntEntity
import org.jetbrains.exposed.v1.dao.IntEntityClass
import org.jetbrains.exposed.v1.datetime.date
import org.jetbrains.exposed.v1.datetime.time

object BookingTable: IntIdTable("booking") {
    val userId = integer("user_id")
    val courtId = integer("court_id")
    val date = date("date")
    val startTime = time("start_time")
    val duration = integer("duration")
}

class BookingDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<BookingDAO>(BookingTable)

    var userId by BookingTable.userId
    var courtId by BookingTable.courtId
    var date by BookingTable.date
    var startTime by BookingTable.startTime
    var duration by BookingTable.duration
}

fun daoToBooking(dao: BookingDAO) = Booking(
    id = dao.id.value,
    userId = dao.userId,
    courtId = dao.courtId,
    date = dao.date,
    startTime = dao.startTime,
    duration = dao.duration
)