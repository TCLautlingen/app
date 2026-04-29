package org.tcl.app.user.data

import org.tcl.app.core.domain.util.DataError
import org.tcl.app.core.domain.util.EmptyResult
import org.tcl.app.core.domain.util.Result
import org.tcl.app.user.DetailedUser
import org.tcl.app.user.User
import org.tcl.app.user.domain.UserRemoteDataSource
import org.tcl.app.user.toUser

class FakeUserRemoteDataSource : UserRemoteDataSource {
    private val users = listOf(
        DetailedUser(id = 1, email = "john.doe@test.com", firstName = "John", lastName = "Doe", isMember = true, isAdmin = true),
        DetailedUser(id = 2, email = "jane.doe@test.com", firstName = "Jane", lastName = "Doe", isMember = true, isAdmin = true),
        DetailedUser(id = 3, email = "admin@test.com", firstName = "Admin", lastName = "User", isMember = true, isAdmin = true),
    )
    private val currentUser = users[0]

    override suspend fun getUsers(searchQuery: String): Result<List<User>, DataError> =
        Result.Success(
            users.filter {
                it.firstName.contains(searchQuery, ignoreCase = true) ||
                        it.lastName.contains(searchQuery, ignoreCase = true) ||
                        it.email.contains(searchQuery, ignoreCase = true)
            }
                .map { it.toUser() }
        )

    override suspend fun getCurrentUser(): Result<DetailedUser, DataError> =
        Result.Success(currentUser)

    override suspend fun getUserById(userId: Int): Result<DetailedUser, DataError> {
        val user = users.find { it.id == userId }
        return if (user != null) Result.Success(user)
        else Result.Error(DataError.NotFound)
    }

    override suspend fun updateCurrentUser(
        firstName: String?,
        lastName: String?,
        phoneNumber: String?,
        address: String?,
    ): Result<DetailedUser, DataError> =
        Result.Success(currentUser)

    override suspend fun adminUpdateUser(
        userId: Int,
        isMember: Boolean?,
        isAdmin: Boolean?,
    ): Result<DetailedUser, DataError> {
        val user = users.find { it.id == userId }
            ?: return Result.Error(DataError.NotFound)
        return Result.Success(user.copy(
            isMember = isMember ?: user.isMember,
            isAdmin = isAdmin ?: user.isAdmin,
        ))
    }
}