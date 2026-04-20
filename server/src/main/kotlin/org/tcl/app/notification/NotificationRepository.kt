package org.tcl.app.notification

interface NotificationRepository {
    suspend fun createNotification(title: String, body: String, senderId: Int): Notification
}