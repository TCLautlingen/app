package org.tcl.app.notification

import kotlinx.serialization.Serializable

@Serializable
data class NotificationTokenRequest(
    val deviceUniqueId: String,
    val notificationToken: String
)