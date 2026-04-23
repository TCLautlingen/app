package org.tcl.app.entities

import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.dao.IntEntity
import org.jetbrains.exposed.v1.dao.IntEntityClass
import org.tcl.app.tables.NotificationTokensTable
import org.tcl.app.tables.ProfilesTable
import org.tcl.app.tables.RefreshTokensTable
import org.tcl.app.tables.UsersTable

class UserEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<UserEntity>(UsersTable)

    var email by UsersTable.email
    var passwordHash by UsersTable.passwordHash
    var passwordSalt by UsersTable.passwordSalt
    var isMember by UsersTable.isMember
    var isAdmin by UsersTable.isAdmin
    val profile by ProfileEntity backReferencedOn ProfilesTable.user
    val refreshTokens by RefreshTokenEntity referencedOn RefreshTokensTable.user
    val notificationTokens by NotificationTokenEntity referrersOn NotificationTokensTable.user
}