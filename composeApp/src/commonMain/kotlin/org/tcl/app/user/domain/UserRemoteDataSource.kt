package org.tcl.app.user.domain

import org.tcl.app.user.AdminUpdateUserRequest
import org.tcl.app.user.UpdateUserRequest
import org.tcl.app.user.User
import org.tcl.app.core.domain.util.DataError
import org.tcl.app.core.domain.util.EmptyResult
import org.tcl.app.core.domain.util.Result
import org.tcl.app.user.DetailedUser

interface UserRemoteDataSource {
    suspend fun getUsers(searchQuery: String): Result<List<User>, DataError>
    suspend fun getCurrentUser(): Result<DetailedUser, DataError>
    suspend fun getUserById(userId: Int): Result<DetailedUser, DataError>
    suspend fun updateCurrentUser(
        firstName: String? = null,
        lastName: String? = null,
        phoneNumber: String? = null,
        address: String? = null,
    ): Result<DetailedUser, DataError>
    suspend fun adminUpdateUser(
        userId: Int,
        isMember: Boolean? = null,
        isAdmin: Boolean? = null,
    ): Result<DetailedUser, DataError>
}