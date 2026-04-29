package org.tcl.app.notification.presentation.inbox

import androidx.compose.runtime.Stable
import org.tcl.app.notification.BroadcastNotification

@Stable
data class NotificationInboxState(
    val notifications: List<BroadcastNotification> = emptyList(),
    val isLoading: Boolean = true,
)
