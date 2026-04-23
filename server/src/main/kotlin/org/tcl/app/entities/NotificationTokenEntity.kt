package org.tcl.app.entities

import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.dao.IntEntity
import org.jetbrains.exposed.v1.dao.IntEntityClass
import org.tcl.app.tables.NotificationTokensTable

class NotificationTokenEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<NotificationTokenEntity>(NotificationTokensTable)

    var token by NotificationTokensTable.token
    var user by UserEntity referencedOn NotificationTokensTable.user
}