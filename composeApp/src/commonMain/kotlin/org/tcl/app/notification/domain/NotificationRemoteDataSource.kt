package org.tcl.app.notification.domain

import org.tcl.app.core.domain.util.DataError
import org.tcl.app.core.domain.util.EmptyResult
import org.tcl.app.core.domain.util.Result
import org.tcl.app.notification.BroadcastNotification

interface NotificationRemoteDataSource {
    suspend fun registerToken(token: String): EmptyResult<DataError>
    suspend fun unregisterToken(token: String): EmptyResult<DataError>
    suspend fun sendNotification(title: String, body: String): EmptyResult<DataError>
    suspend fun getInbox(): Result<List<BroadcastNotification>, DataError>
}