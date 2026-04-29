package org.tcl.app.notification.data

import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import org.tcl.app.notification.BroadcastNotification
import org.tcl.app.notification.SendNotificationRequest
import org.tcl.app.core.data.network.BackendApiClient
import org.tcl.app.core.domain.util.DataError
import org.tcl.app.core.domain.util.EmptyResult
import org.tcl.app.core.domain.util.Result
import org.tcl.app.core.domain.util.safeApiCall
import org.tcl.app.notification.RegisterNotificationTokenRequest
import org.tcl.app.notification.domain.NotificationRemoteDataSource

class KtorNotificationRemoteDataSource(
    private val backendApiClient: BackendApiClient
) : NotificationRemoteDataSource {

    override suspend fun registerToken(token: String): EmptyResult<DataError> = safeApiCall {
        backendApiClient.client.post("notifications/register") {
            setBody(RegisterNotificationTokenRequest(token))
        }
    }

    override suspend fun unregisterToken(token: String): EmptyResult<DataError> = safeApiCall {
        backendApiClient.client.delete("notifications/$token")
    }

    override suspend fun sendNotification(title: String, body: String): EmptyResult<DataError> = safeApiCall {
        backendApiClient.client.post("notifications/send") {
            setBody(SendNotificationRequest(
                title = title,
                body = body
            ))
        }
    }

    override suspend fun getInbox(): Result<List<BroadcastNotification>, DataError> = safeApiCall {
        backendApiClient.client.get("notifications/inbox")
    }
}