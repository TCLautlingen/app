package org.tcl.app.models

import kotlinx.datetime.LocalDateTime

data class Notification(
    val id: Int,
    val title: String,
    val body: String,
    val createdAt: LocalDateTime,
    val createdBy: Int,
)
