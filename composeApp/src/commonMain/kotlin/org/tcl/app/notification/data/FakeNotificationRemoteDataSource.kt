package org.tcl.app.notification.data

import org.tcl.app.core.domain.util.DataError
import org.tcl.app.core.domain.util.EmptyResult
import org.tcl.app.core.domain.util.Result
import org.tcl.app.notification.domain.NotificationRemoteDataSource

class FakeNotificationRemoteDataSource : NotificationRemoteDataSource {
    override suspend fun sendNotification(
        title: String,
        body: String
    ): EmptyResult<DataError> {
        return Result.Success(Unit)
    }
}