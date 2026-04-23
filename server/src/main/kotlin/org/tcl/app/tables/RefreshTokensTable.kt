package org.tcl.app.tables

import org.jetbrains.exposed.v1.core.dao.id.IntIdTable

object RefreshTokensTable : IntIdTable() {
    val token = varchar("token", 512).uniqueIndex()
    val expiresAt = long("expires_at")
    val user = reference("user", UsersTable)
}