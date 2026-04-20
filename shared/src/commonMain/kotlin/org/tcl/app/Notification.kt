package org.tcl.app

import kotlinx.serialization.Serializable

@Serializable
data class SendNotificationRequest(
    val title: String,
    val body: String
)

@Serializable
data class InboxNotification(
    val id: Int,
    val title: String,
    val body: String,
    val createdAt: Long,
    val isRead: Boolean
)