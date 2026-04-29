package org.tcl.app.repositories.fake

import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.tcl.app.notification.BroadcastNotification
import org.tcl.app.repositories.NotificationRepository
import org.tcl.app.repositories.UserRepository
import kotlin.time.Clock

class FakeNotificationRepository(
    private val userRepository: UserRepository
) : NotificationRepository {
    private val notifications = mutableListOf<BroadcastNotification>()
    private var nextId = 1

    override suspend fun createNotification(
        title: String,
        body: String,
        senderId: Int
    ): BroadcastNotification {
        val user = userRepository.userById(senderId)!!
        val notification = BroadcastNotification(
            id = nextId++,
            title = title,
            body = body,
            createdAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
            createdByFirstName = user.firstName,
            createdByLastName = user.lastName,
        )
        notifications.add(notification)
        return notification
    }

    override suspend fun getAllNotifications(): List<BroadcastNotification> {
        return notifications.sortedByDescending { it.createdAt }
    }
}
