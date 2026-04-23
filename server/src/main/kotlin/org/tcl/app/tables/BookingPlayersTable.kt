package org.tcl.app.tables

import org.jetbrains.exposed.v1.core.Table

object BookingPlayersTable : Table() {
    val booking = reference("booking", BookingsTable)
    val user = reference("user", UsersTable)
    override val primaryKey = PrimaryKey(booking, user)
}