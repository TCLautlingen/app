package org.tcl.app.notification

import kotlinx.serialization.Serializable

@Serializable
data class RegisterNotificationTokenRequest(
    val token: String
)