package org.tcl.app.entities

import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.dao.IntEntity
import org.jetbrains.exposed.v1.dao.IntEntityClass
import org.tcl.app.tables.NotificationsTable

class NotificationEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<NotificationEntity>(NotificationsTable)

    var title by NotificationsTable.title
    var body by NotificationsTable.body
    var createdAt by NotificationsTable.createdAt
    var createdBy by UserEntity referencedOn NotificationsTable.createdBy
}