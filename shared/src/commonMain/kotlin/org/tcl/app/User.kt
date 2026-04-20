package org.tcl.app

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: Int,
    val email: String,
    val firstName: String,
    val lastName: String,
    val isMember: Boolean,
    val isAdmin: Boolean,
)

@Serializable
data class NotificationTokenRequest(
    val deviceUniqueId: String,
    val notificationToken: String
)