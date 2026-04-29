package org.tcl.app.db.tables

import org.jetbrains.exposed.v1.core.dao.id.IntIdTable
import org.jetbrains.exposed.v1.datetime.date
import org.jetbrains.exposed.v1.datetime.time

object BookingsTable : IntIdTable("bookings") {
    val date = date("date")
    val startTime = time("start_time")
    val duration = integer("duration")
    val user = reference("user", UsersTable)
    val court = reference("court", CourtsTable)
}