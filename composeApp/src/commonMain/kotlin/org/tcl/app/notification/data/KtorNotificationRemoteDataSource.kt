package org.tcl.app.notification.data

import io.ktor.client.request.post
import io.ktor.client.request.setBody
import org.tcl.app.SendNotificationRequest
import org.tcl.app.core.data.ApiClient
import org.tcl.app.core.domain.util.DataError
import org.tcl.app.core.domain.util.EmptyResult
import org.tcl.app.core.domain.util.safeApiCall
import org.tcl.app.notification.domain.NotificationRemoteDataSource

class KtorNotificationRemoteDataSource(
    private val apiClient: ApiClient
) : NotificationRemoteDataSource {
    override suspend fun sendNotification(title: String, body: String): EmptyResult<DataError> = safeApiCall {
        apiClient.client.post("/notifications/send") {
            setBody(SendNotificationRequest(
                title = title,
                body = body
            ))
        }
    }
}