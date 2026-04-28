package org.tcl.app.repositories.postgres

import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.tcl.app.entities.NotificationEntity
import org.tcl.app.entities.UserEntity
import org.tcl.app.mappers.entityToNotification
import org.tcl.app.models.Notification
import org.tcl.app.repositories.NotificationRepository

class PostgresNotificationRepository : NotificationRepository {
    override suspend fun createNotification(
        title: String,
        body: String,
        senderId: Int
    ): Notification = transaction {
        NotificationEntity
            .new {
                this.title = title
                this.body = body
                this.createdBy = UserEntity[senderId]
            }.let(::entityToNotification)
    }
}
