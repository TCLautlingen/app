package org.tcl.app.notification

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class BroadcastNotification(
    val id: Int,
    val title: String,
    val body: String,
    val createdAt: LocalDateTime,
    val createdByFirstName: String,
    val createdByLastName: String,
)
