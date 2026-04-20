package org.tcl.app.notification

import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock

class FakeNotificationRepository : NotificationRepository {
    private val notifications = mutableListOf<Notification>()
    private var nextId = 1

    override suspend fun createNotification(
        title: String,
        body: String,
        senderId: Int
    ): Notification {
        val now = Clock.System.now()
        val tz = TimeZone.currentSystemDefault()

        val notification = Notification(
            id = nextId++,
            title = title,
            body = body,
            createdAt = now.toLocalDateTime(tz),
            createdBy = senderId
        )
        notifications.add(notification)
        return notification
    }
}