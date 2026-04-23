package org.tcl.app.services

import org.tcl.app.models.Notification
import org.tcl.app.repositories.DeviceRepository
import org.tcl.app.repositories.NotificationRepository
import org.tcl.app.repositories.UserRepository

class NotificationService(
    private val notificationRepository: NotificationRepository,
    private val deviceRepository: DeviceRepository,
    private val userRepository: UserRepository,
    private val firebaseService: FirebaseService
) {
    suspend fun sendToAll(title: String, body: String, senderId: Int) {
        val notification = notificationRepository.createNotification(title, body, senderId)

        /*
        notificationRepository.createInboxEntries(
            notificationId = notification.id,
            userIds = allUsers.map { it.id }
        )
         */

        val allDevices = deviceRepository.getAllDevices()
        val allTokens = allDevices.map { it.notificationToken }

        firebaseService.sendToTokens(
            tokens = allTokens,
            title = title,
            body = body,
            data = mapOf("notificationId" to notification.toString())
        )
    }
}
