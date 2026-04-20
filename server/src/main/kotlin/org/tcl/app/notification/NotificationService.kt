package org.tcl.app.notification

import org.tcl.app.device.DeviceRepository
import org.tcl.app.firebase.FirebaseService
import org.tcl.app.user.UserRepository

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