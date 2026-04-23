package org.tcl.app.user.data

import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import org.tcl.app.user.User
import org.tcl.app.core.data.ApiClient
import org.tcl.app.core.domain.util.DataError
import org.tcl.app.core.domain.util.EmptyResult
import org.tcl.app.core.domain.util.Result
import org.tcl.app.core.domain.util.safeApiCall
import org.tcl.app.notification.NotificationTokenRequest
import org.tcl.app.user.domain.UserRemoteDataSource

class KtorUserRemoteDataSource(
    private val apiClient: ApiClient
) : UserRemoteDataSource {
    override suspend fun getUsers(searchQuery: String): Result<List<User>, DataError> = safeApiCall {
        apiClient.client.get("/users") {
            if (searchQuery.isNotBlank()) {
                parameter("searchQuery", searchQuery)
            }
        }
    }

    override suspend fun getCurrentUser(): Result<User, DataError> = safeApiCall {
        apiClient.client.get("/users/me")
    }

    override suspend fun getUserById(userId: Int): Result<User, DataError> = safeApiCall {
        apiClient.client.get("/users/$userId").body()
    }
}