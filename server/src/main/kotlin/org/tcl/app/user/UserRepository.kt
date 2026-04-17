package org.tcl.app.user

import org.tcl.app.User

interface UserRepository {
    suspend fun createUser(
        email: String,
        passwordHash: String,
        passwordSalt: String,
        firstName: String,
        lastName: String
    ) : User

    suspend fun allUsers(searchQuery: String): List<User>

    suspend fun userById(id: Int): User?

    suspend fun authUserByEmail(email: String): AuthUser?
}