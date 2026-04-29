package org.tcl.app.services

import org.tcl.app.notification.BroadcastNotification
import org.tcl.app.repositories.NotificationRepository
import org.tcl.app.repositories.NotificationTokenRepository
import org.tcl.app.repositories.UserRepository

class NotificationService(
    private val notificationRepository: NotificationRepository,
    private val notificationTokenRepository: NotificationTokenRepository,
    private val userRepository: UserRepository,
    private val firebaseService: FirebaseService
) {
    suspend fun registerNotificationToken(userId: Int, token: String) {
        notificationTokenRepository.registerToken(userId, token)
    }

    suspend fun removeNotificationToken(userId: Int, token: String) {
        notificationTokenRepository.removeToken(userId, token)
    }

    suspend fun sendToAll(title: String, body: String, senderId: Int) {
        val notification = notificationRepository.createNotification(title, body, senderId)

        val allTokens = notificationTokenRepository.getAllTokens()

        firebaseService.sendToTokens(
            tokens = allTokens,
            title = title,
            body = body,
            data = mapOf("notificationId" to notification.id.toString())
        )
    }

    suspend fun getBroadcastNotifications(): List<BroadcastNotification> {
        return notificationRepository.getAllNotifications()
    }
}
