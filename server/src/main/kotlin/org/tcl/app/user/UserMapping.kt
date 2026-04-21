package org.tcl.app.user

import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.core.dao.id.IntIdTable
import org.jetbrains.exposed.v1.dao.IntEntity
import org.jetbrains.exposed.v1.dao.IntEntityClass

object UserTable: IntIdTable("user") {
    val email = varchar("email", 255).uniqueIndex()
    val passwordHash = varchar("password_hash", 255)
    val passwordSalt = varchar("password_salt", 255)
    val firstName = varchar("first_name", 255)
    val lastName = varchar("last_name", 255)
    val isMember = bool("is_member").default(false)
    val isAdmin = bool("is_admin").default(false)
}

class UserDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<UserDAO>(UserTable)

    var email by UserTable.email
    var passwordHash by UserTable.passwordHash
    var passwordSalt by UserTable.passwordSalt
    var firstName by UserTable.firstName
    var lastName by UserTable.lastName
    var isMember by UserTable.isMember
    var isAdmin by UserTable.isAdmin
}

fun daoToUser(dao: UserDAO) = User(
    dao.id.value,
    dao.email,
    dao.firstName,
    dao.lastName,
    dao.isMember,
    dao.isAdmin,
)

fun daoToAuthUser(dao: UserDAO) = AuthUser(
    dao.id.value,
    dao.email,
    dao.passwordHash,
    dao.passwordSalt,
    dao.firstName,
    dao.lastName
)