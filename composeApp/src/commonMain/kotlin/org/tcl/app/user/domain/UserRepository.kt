package org.tcl.app.user.domain

import org.tcl.app.User
import org.tcl.app.core.domain.util.DataError
import org.tcl.app.core.domain.util.EmptyResult
import org.tcl.app.core.domain.util.Result

interface UserRepository {
    suspend fun getUsers(searchQuery: String): Result<List<User>, DataError>
    suspend fun getCurrentUser(): Result<User, DataError>
    suspend fun updateDeviceToken(deviceToken: String): EmptyResult<DataError>
    suspend fun getUserById(userId: Int): Result<User, DataError>
}