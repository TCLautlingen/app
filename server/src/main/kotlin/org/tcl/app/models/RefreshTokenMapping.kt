package org.tcl.app.models

import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.core.dao.id.IntIdTable
import org.jetbrains.exposed.v1.dao.IntEntity
import org.jetbrains.exposed.v1.dao.IntEntityClass

object RefreshTokenTable : IntIdTable("refresh_token") {
    val userId = reference("user_id", UserTable)
    val token = varchar("token", 512).uniqueIndex()
    val expiresAt = long("expires_at")
}

class RefreshTokenDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<RefreshTokenDAO>(RefreshTokenTable)

    var user by UserDAO referencedOn RefreshTokenTable.userId
    var token by RefreshTokenTable.token
    var expiresAt by RefreshTokenTable.expiresAt
}

fun daoToRefreshToken(dao: RefreshTokenDAO) = RefreshToken(
    dao.token,
    dao.user.id.value,
    dao.expiresAt
)
