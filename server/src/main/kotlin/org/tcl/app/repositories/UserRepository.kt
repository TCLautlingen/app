package org.tcl.app.repositories

import org.tcl.app.models.AuthUser
import org.tcl.app.user.User

interface UserRepository {
    suspend fun createUser(
        email: String,
        passwordHash: String,
        passwordSalt: String
    ): User?

    suspend fun allUsers(searchQuery: String): List<User>

    suspend fun userById(id: Int): User?

    suspend fun authUserByEmail(email: String): AuthUser?
}
