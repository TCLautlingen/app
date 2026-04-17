package org.tcl.app.user.data

import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import org.tcl.app.User
import org.tcl.app.core.data.ApiClient

class UserApiService(
    private val apiClient: ApiClient
) {
    suspend fun getUsers(searchQuery: String): List<User> {
        return apiClient.client.get("/users") {
            if (searchQuery.isNotBlank()) {
                parameter("searchQuery", searchQuery)
            }
        }.body()
    }

    suspend fun getCurrentUser(): User {
        return apiClient.client.get("/users/me").body()
    }

    suspend fun getUserById(userId: Int): User {
        return apiClient.client.get("/users/$userId").body()
    }
}