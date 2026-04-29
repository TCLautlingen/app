package org.tcl.app.mappers

import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.tcl.app.db.entities.NotificationEntity
import org.tcl.app.notification.BroadcastNotification

fun entityToBroadcastNotification(entity: NotificationEntity): BroadcastNotification = BroadcastNotification(
    id = entity.id.value,
    title = entity.title,
    body = entity.body,
    createdAt = entity.createdAt.toLocalDateTime(TimeZone.of("Europe/Berlin")),
    createdByFirstName = entity.createdBy.profile?.firstName ?: "",
    createdByLastName = entity.createdBy.profile?.lastName ?: "",
)