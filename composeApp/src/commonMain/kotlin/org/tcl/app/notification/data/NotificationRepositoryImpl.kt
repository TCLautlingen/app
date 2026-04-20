package org.tcl.app.notification.data

import org.tcl.app.core.domain.util.DataError
import org.tcl.app.core.domain.util.EmptyResult
import org.tcl.app.notification.domain.NotificationRepository

class NotificationRepositoryImpl(
    private val api: NotificationApiService
) : NotificationRepository {
    override suspend fun sendNotification(title: String, body: String): EmptyResult<DataError> =
        api.sendNotification(title, body)
}