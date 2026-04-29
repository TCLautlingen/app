package org.tcl.app.db.entities

import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.dao.IntEntity
import org.jetbrains.exposed.v1.dao.IntEntityClass
import org.tcl.app.db.tables.NotificationTokensTable

class NotificationTokenEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<NotificationTokenEntity>(NotificationTokensTable)

    var token by NotificationTokensTable.token
    var user by UserEntity referencedOn NotificationTokensTable.user
}