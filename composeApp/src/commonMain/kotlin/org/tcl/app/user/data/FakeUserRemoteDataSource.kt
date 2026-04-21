package org.tcl.app.user.data

import org.tcl.app.User
import org.tcl.app.core.domain.util.DataError
import org.tcl.app.core.domain.util.EmptyResult
import org.tcl.app.core.domain.util.Result
import org.tcl.app.user.domain.UserRemoteDataSource

class FakeUserRemoteDataSource : UserRemoteDataSource {
    private val users = listOf(
        User(id = 1, email = "john.doe@test.com", firstName = "John", lastName = "Doe", isMember = true, isAdmin = true),
        User(id = 2, email = "jane.doe@test.com", firstName = "Jane", lastName = "Doe", isMember = true, isAdmin = false),
        User(id = 3, email = "admin@test.com", firstName = "Admin", lastName = "User", isMember = true, isAdmin = false),
    )
    private val currentUser = users[0]

    override suspend fun getUsers(searchQuery: String): Result<List<User>, DataError> =
        Result.Success(
            users.filter {
                it.firstName.contains(searchQuery, ignoreCase = true) ||
                        it.lastName.contains(searchQuery, ignoreCase = true) ||
                        it.email.contains(searchQuery, ignoreCase = true)
            }
        )

    override suspend fun getCurrentUser(): Result<User, DataError> =
        Result.Success(currentUser)

    override suspend fun updateNotificationToken(
        deviceUniqueId: String,
        notificationToken: String
    ): EmptyResult<DataError> = Result.Success(Unit)

    override suspend fun getUserById(userId: Int): Result<User, DataError> {
        val user = users.find { it.id == userId }
        return if (user != null) Result.Success(user)
        else Result.Error(DataError.NotFound)
    }
}