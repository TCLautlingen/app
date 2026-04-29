package org.tcl.app.notification.data

import org.tcl.app.core.domain.util.DataError
import org.tcl.app.core.domain.util.EmptyResult
import org.tcl.app.core.domain.util.Result
import org.tcl.app.notification.BroadcastNotification
import org.tcl.app.notification.domain.NotificationRemoteDataSource

class FakeNotificationRemoteDataSource : NotificationRemoteDataSource {
    override suspend fun registerToken(token: String): EmptyResult<DataError> {
        return Result.Success(Unit)
    }

    override suspend fun unregisterToken(token: String): EmptyResult<DataError> {
        return Result.Success(Unit)
    }

    override suspend fun sendNotification(
        title: String,
        body: String
    ): EmptyResult<DataError> {
        return Result.Success(Unit)
    }

    override suspend fun getInbox(): Result<List<BroadcastNotification>, DataError> {
        return Result.Success(emptyList())
    }
}