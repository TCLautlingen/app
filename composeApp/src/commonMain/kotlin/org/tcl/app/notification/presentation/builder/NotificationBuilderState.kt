package org.tcl.app.notification.presentation.builder

import androidx.compose.runtime.Stable

@Stable
data class NotificationBuilderState(
    val title: String = "",
    val body: String = "",
)