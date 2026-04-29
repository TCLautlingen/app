package org.tcl.app.user.data

import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.patch
import io.ktor.client.request.setBody
import org.tcl.app.user.AdminUpdateUserRequest
import org.tcl.app.user.UpdateUserRequest
import org.tcl.app.user.User
import org.tcl.app.core.data.network.BackendApiClient
import org.tcl.app.core.domain.util.DataError
import org.tcl.app.core.domain.util.Result
import org.tcl.app.core.domain.util.safeApiCall
import org.tcl.app.user.DetailedUser
import org.tcl.app.user.domain.UserRemoteDataSource

class KtorUserRemoteDataSource(
    private val backendApiClient: BackendApiClient
) : UserRemoteDataSource {
    override suspend fun getUsers(searchQuery: String): Result<List<User>, DataError> = safeApiCall {
        backendApiClient.client.get("users") {
            if (searchQuery.isNotBlank()) {
                parameter("searchQuery", searchQuery)
            }
        }
    }

    override suspend fun getCurrentUser(): Result<DetailedUser, DataError> = safeApiCall {
        backendApiClient.client.get("users/me")
    }

    override suspend fun getUserById(userId: Int): Result<DetailedUser, DataError> = safeApiCall {
        backendApiClient.client.get("users/$userId").body()
    }

    override suspend fun updateCurrentUser(
        firstName: String?,
        lastName: String?,
        phoneNumber: String?,
        address: String?,
    ): Result<DetailedUser, DataError> = safeApiCall {
        backendApiClient.client.patch("users/me") {
            setBody(UpdateUserRequest(firstName, lastName, phoneNumber, address))
        }
    }

    override suspend fun adminUpdateUser(
        userId: Int,
        isMember: Boolean?,
        isAdmin: Boolean?,
    ): Result<DetailedUser, DataError> = safeApiCall {
        backendApiClient.client.patch("users/$userId") {
            setBody(AdminUpdateUserRequest(isMember, isAdmin))
        }
    }
}