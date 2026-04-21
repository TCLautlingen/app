package org.tcl.app.notification.presentation.builder

sealed interface NotificationBuilderEvent {
    data object NotificationSent : NotificationBuilderEvent
}