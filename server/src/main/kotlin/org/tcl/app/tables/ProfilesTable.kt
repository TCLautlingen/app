package org.tcl.app.tables

import org.jetbrains.exposed.v1.core.dao.id.IntIdTable

object ProfilesTable : IntIdTable() {
    val firstName = varchar("first_name", 256)
    val lastName = varchar("last_name", 256)
    val phoneNumber = varchar("phone_number", 20).nullable()
    val address = varchar("address", 256).nullable()
    val user = reference("user", UsersTable)
}