package org.tcl.app.auth

import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.core.dao.id.java.UUIDTable
import org.jetbrains.exposed.v1.dao.java.UUIDEntity
import org.jetbrains.exposed.v1.dao.java.UUIDEntityClass
import org.tcl.app.user.UserDAO
import org.tcl.app.user.UserTable
import java.util.UUID


object RefreshTokenTable : UUIDTable("refresh_token") {
    val userId = reference("user_id", UserTable)
    val token = varchar("token", 512).uniqueIndex()
    val expiresAt = long("expires_at")
}

class RefreshTokenDAO(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<RefreshTokenDAO>(RefreshTokenTable)

    var user by UserDAO referencedOn RefreshTokenTable.userId
    var token by RefreshTokenTable.token
    var expiresAt by RefreshTokenTable.expiresAt
}

fun daoToRefreshToken(dao: RefreshTokenDAO) = RefreshToken(
    dao.token,
    dao.user.id.value,
    dao.expiresAt
)

