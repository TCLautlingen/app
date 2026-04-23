package org.tcl.app.models

import kotlinx.datetime.LocalDateTime
import org.tcl.app.user.User

data class Notification(
    val id: Int,
    val title: String,
    val body: String,
    val createdAt: LocalDateTime,
    val createdBy: User
)
