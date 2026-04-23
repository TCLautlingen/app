package org.tcl.app.models

import org.jetbrains.exposed.v1.core.Table

object BookingPlayerTable : Table("booking_player") {
    val bookingId = integer("booking_id").references(BookingTable.id)
    val userId = integer("user_id").references(UserTable.id)
    override val primaryKey = PrimaryKey(bookingId, userId)
}
