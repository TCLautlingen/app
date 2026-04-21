package org.tcl.app.notification.domain

import org.tcl.app.core.domain.util.DataError
import org.tcl.app.core.domain.util.EmptyResult

interface NotificationRemoteDataSource {
    suspend fun sendNotification(title: String, body: String): EmptyResult<DataError>
}