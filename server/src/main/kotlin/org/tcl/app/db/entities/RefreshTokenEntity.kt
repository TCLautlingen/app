package org.tcl.app.db.entities

import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.dao.IntEntity
import org.jetbrains.exposed.v1.dao.IntEntityClass
import org.tcl.app.db.tables.RefreshTokensTable

class RefreshTokenEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<RefreshTokenEntity>(RefreshTokensTable)

    var token by RefreshTokensTable.token
    var expiresAt by RefreshTokensTable.expiresAt
    var user by UserEntity referencedOn RefreshTokensTable.user
}