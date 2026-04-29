package org.tcl.app.db.tables

import org.jetbrains.exposed.v1.core.dao.id.IntIdTable

object UsersTable : IntIdTable("users") {
    val email = varchar("email", 256).uniqueIndex()
    val passwordHash = varchar("password_hash", 256)
    val passwordSalt = varchar("password_salt", 256)
    val isMember = bool("is_member").default(false)
    val isAdmin = bool("is_admin").default(false)
}