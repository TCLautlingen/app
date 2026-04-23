package org.tcl.app.tables

import org.jetbrains.exposed.v1.core.dao.id.IntIdTable
import org.jetbrains.exposed.v1.datetime.CurrentTimestamp
import org.jetbrains.exposed.v1.datetime.timestamp

object NotificationsTable : IntIdTable("notifications") {
    val title = varchar("title", 256)
    val body = text("body")
    val createdAt = timestamp("created_at").defaultExpression(CurrentTimestamp)
    val createdBy = reference("created_by", UsersTable)
}