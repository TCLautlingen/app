package org.tcl.app.notification.presentation.builder

sealed interface NotificationBuilderAction {
    data class OnTitleChange(val title: String) : NotificationBuilderAction
    data class OnBodyChange(val body: String) : NotificationBuilderAction
    data object OnSendClick : NotificationBuilderAction
}