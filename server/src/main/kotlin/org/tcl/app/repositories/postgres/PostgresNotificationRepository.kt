package org.tcl.app.repositories.postgres

import org.tcl.app.db.entities.NotificationEntity
import org.tcl.app.db.entities.UserEntity
import org.tcl.app.db.withTransaction
import org.tcl.app.mappers.entityToBroadcastNotification
import org.tcl.app.notification.BroadcastNotification
import org.tcl.app.repositories.NotificationRepository

class PostgresNotificationRepository : NotificationRepository {
    override suspend fun createNotification(
        title: String,
        body: String,
        senderId: Int
    ): BroadcastNotification = withTransaction {
        NotificationEntity
            .new {
                this.title = title
                this.body = body
                this.createdBy = UserEntity[senderId]
            }.let(::entityToBroadcastNotification)
    }

    override suspend fun getAllNotifications(): List<BroadcastNotification> = withTransaction {
        NotificationEntity.all()
            .sortedByDescending { it.createdAt }
            .map(::entityToBroadcastNotification)
    }
}
