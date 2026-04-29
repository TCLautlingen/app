package org.tcl.app.db.tables

import org.jetbrains.exposed.v1.core.dao.id.IntIdTable

object RefreshTokensTable : IntIdTable("refresh_tokens") {
    val token = varchar("token", 512).uniqueIndex()
    val expiresAt = long("expires_at")
    val user = reference("user", UsersTable)
}