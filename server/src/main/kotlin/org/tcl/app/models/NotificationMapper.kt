package org.tcl.app.models

import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.core.dao.id.IntIdTable
import org.jetbrains.exposed.v1.dao.IntEntity
import org.jetbrains.exposed.v1.dao.IntEntityClass
import org.jetbrains.exposed.v1.datetime.CurrentTimestamp
import org.jetbrains.exposed.v1.datetime.timestamp
import org.tcl.app.entities.UserEntity
import org.tcl.app.tables.UsersTable

object NotificationTable : IntIdTable("notification") {
    val title = varchar("title", 256)
    val body = text("body")
    val createdAt = timestamp("created_at").defaultExpression(CurrentTimestamp)
    val createdBy = reference("created_by", UsersTable)
}

class NotificationDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<NotificationDAO>(NotificationTable)

    var title by NotificationTable.title
    var body by NotificationTable.body
    var createdAt by NotificationTable.createdAt
    var createdBy by UserEntity referencedOn NotificationTable.createdBy
}

fun daoToNotification(dao: NotificationDAO): Notification {
    return Notification(
        id = dao.id.value,
        title = dao.title,
        body = dao.body,
        createdAt = dao.createdAt.toLocalDateTime(TimeZone.UTC),
        createdBy = dao.createdBy.id.value
    )
}
