package org.tcl.app.models

data class Device(
    val userId: Int,
    val deviceUniqueId: String,
    var notificationToken: String,
)
