package org.tcl.app.notification.presentation.inbox

sealed interface NotificationInboxAction {
    data object OnRefresh : NotificationInboxAction
}
