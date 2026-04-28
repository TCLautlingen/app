package org.tcl.app.repositories

import org.tcl.app.models.AuthUser
import org.tcl.app.user.DetailedUser
import org.tcl.app.user.User

interface UserRepository {
    suspend fun createUser(
        email: String,
        passwordHash: String,
        passwordSalt: String
    ): User?

    suspend fun updateUser(
        id: Int,
        firstName: String?,
        lastName: String?,
        phoneNumber: String?,
        address: String?,
        isMember: Boolean?,
    ): DetailedUser?

    suspend fun allUsers(searchQuery: String): List<User>

    suspend fun userById(id: Int): User?
    suspend fun detailedUserById(id: Int): DetailedUser?
    suspend fun authUserById(id: Int): AuthUser?
    suspend fun authUserByEmail(email: String): AuthUser?
}
