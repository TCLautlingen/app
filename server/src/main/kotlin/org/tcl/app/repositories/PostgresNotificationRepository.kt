package org.tcl.app.repositories

import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.tcl.app.entities.UserEntity
import org.tcl.app.models.Notification
import org.tcl.app.models.NotificationDAO
import org.tcl.app.models.daoToNotification

class PostgresNotificationRepository : NotificationRepository {
    override suspend fun createNotification(
        title: String,
        body: String,
        senderId: Int
    ): Notification = transaction {
        NotificationDAO
            .new {
                this.title = title
                this.body = body
                this.createdBy = UserEntity[senderId]
            }.let(::daoToNotification)
    }
}
