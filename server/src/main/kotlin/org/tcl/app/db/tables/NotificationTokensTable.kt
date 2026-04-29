package org.tcl.app.db.tables

import org.jetbrains.exposed.v1.core.dao.id.IntIdTable

object NotificationTokensTable : IntIdTable("notification_tokens") {
    val token = varchar("token", 256)
    val user = reference("user", UsersTable)
}