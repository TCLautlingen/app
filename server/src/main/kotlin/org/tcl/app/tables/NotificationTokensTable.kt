package org.tcl.app.tables

import org.jetbrains.exposed.v1.core.dao.id.IntIdTable

object NotificationTokensTable : IntIdTable() {
    val token = varchar("token", 256)
    val user = reference("user", UsersTable)
}