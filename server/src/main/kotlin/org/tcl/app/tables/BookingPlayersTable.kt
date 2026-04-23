package org.tcl.app.tables

import org.jetbrains.exposed.v1.core.ReferenceOption
import org.jetbrains.exposed.v1.core.Table

object BookingPlayersTable : Table("booking_players") {
    val booking = reference("booking", BookingsTable, onDelete = ReferenceOption.CASCADE)
    val user = reference("user", UsersTable)
    override val primaryKey = PrimaryKey(booking, user)
}