package org.tcl.app.device

data class Device(
    val userId: Int,
    val deviceUniqueId: String,
    var notificationToken: String,
)
