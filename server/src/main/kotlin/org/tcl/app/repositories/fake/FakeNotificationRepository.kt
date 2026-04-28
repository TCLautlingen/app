package org.tcl.app.repositories.fake

import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.tcl.app.models.Notification
import org.tcl.app.repositories.NotificationRepository
import org.tcl.app.repositories.UserRepository
import kotlin.time.Clock

class FakeNotificationRepository(
    private val userRepository: UserRepository
) : NotificationRepository {
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
            createdBy = userRepository.userById(senderId)!!
        )
        notifications.add(notification)
        return notification
    }
}
