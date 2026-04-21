package org.tcl.app.notification

import kotlinx.serialization.Serializable

@Serializable
data class SendNotificationRequest(
    val title: String,
    val body: String
)