package org.tcl.app.user.data

import org.tcl.app.User
import org.tcl.app.core.domain.util.DataError
import org.tcl.app.core.domain.util.EmptyResult
import org.tcl.app.core.domain.util.Result
import org.tcl.app.user.domain.UserRepository

class UserRepositoryImpl(
    private val api: UserApiService
) : UserRepository {
    override suspend fun getUsers(searchQuery: String): Result<List<User>, DataError> = api.getUsers(searchQuery)

    override suspend fun getCurrentUser(): Result<User, DataError> = api.getCurrentUser()

    override suspend fun updateNotificationToken(deviceUniqueId: String, notificationToken: String): EmptyResult<DataError> =
        api.updateNotificationToken(deviceUniqueId, notificationToken)

    override suspend fun getUserById(userId: Int): Result<User, DataError> = api.getUserById(userId)
}