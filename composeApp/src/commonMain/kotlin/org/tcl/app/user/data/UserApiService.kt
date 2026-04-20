package org.tcl.app.user.data

import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import org.tcl.app.User
import org.tcl.app.core.data.ApiClient
import org.tcl.app.core.domain.util.DataError
import org.tcl.app.core.domain.util.Result
import org.tcl.app.core.domain.util.safeApiCall

class UserApiService(
    private val apiClient: ApiClient
) {
    suspend fun getUsers(searchQuery: String): Result<List<User>, DataError> = safeApiCall {
        apiClient.client.get("/users") {
            if (searchQuery.isNotBlank()) {
                parameter("searchQuery", searchQuery)
            }
        }
    }

    suspend fun getCurrentUser(): Result<User, DataError> = safeApiCall {
        apiClient.client.get("/users/me")
    }

    suspend fun getUserById(userId: Int): Result<User, DataError> = safeApiCall {
        apiClient.client.get("/users/$userId").body()
    }
}