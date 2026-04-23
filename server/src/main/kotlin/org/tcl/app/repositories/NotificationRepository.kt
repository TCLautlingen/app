package org.tcl.app.repositories

import org.tcl.app.models.Notification

interface NotificationRepository {
    suspend fun createNotification(title: String, body: String, senderId: Int): Notification
}
