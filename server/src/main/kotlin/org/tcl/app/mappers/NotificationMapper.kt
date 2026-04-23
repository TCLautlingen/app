package org.tcl.app.mappers

import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.tcl.app.entities.NotificationEntity
import org.tcl.app.models.Notification

fun entityToNotification(entity: NotificationEntity): Notification = Notification(
    id = entity.id.value,
    title = entity.title,
    body = entity.body,
    createdAt = entity.createdAt.toLocalDateTime(TimeZone.UTC),
    createdBy = entity.createdBy.let(::entityToUser)
)