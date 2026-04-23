package org.tcl.app.entities

import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.dao.IntEntity
import org.jetbrains.exposed.v1.dao.IntEntityClass
import org.tcl.app.tables.BookingPlayersTable
import org.tcl.app.tables.BookingsTable

class BookingEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<BookingEntity>(BookingsTable)

    var date by BookingsTable.date
    var startTime by BookingsTable.startTime
    var duration by BookingsTable.duration
    var user by UserEntity referencedOn BookingsTable.user
    var court by CourtEntity referencedOn BookingsTable.court
    var players by UserEntity via BookingPlayersTable
}