package org.tcl.app.repositories

import org.tcl.app.notification.BroadcastNotification

interface NotificationRepository {
    suspend fun createNotification(title: String, body: String, senderId: Int): BroadcastNotification
    suspend fun getAllNotifications(): List<BroadcastNotification>
}
