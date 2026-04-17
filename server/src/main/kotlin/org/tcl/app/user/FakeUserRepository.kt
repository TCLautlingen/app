package org.tcl.app.user

import org.tcl.app.User

class FakeUserRepository : UserRepository {
    private data class StoredUser(
        val id: Int,
        val email: String,
        val passwordHash: String,
        val passwordSalt: String,
        val firstName: String,
        val lastName: String,
        val isMember: Boolean,
        val isAdmin: Boolean,
    ) {
        fun toUser(): User = User(id, email, firstName, lastName, isMember, isAdmin)

        fun toAuthUser(): AuthUser = AuthUser(id, email, passwordHash, passwordSalt, firstName, lastName)
    }

    private var nextId = 1

    private val users = mutableListOf<StoredUser>()

    override suspend fun createUser(
        email: String,
        passwordHash: String,
        passwordSalt: String,
        firstName: String,
        lastName: String
    ): User {
        val id = nextId++
        val storedUser = StoredUser(id, email, passwordHash, passwordSalt, firstName, lastName,
            isMember = true,
            isAdmin = false,
        )
        users.add(storedUser)

        return storedUser.toUser()
    }

    override suspend fun allUsers(searchQuery: String): List<User> {
        return users
            .filter { it.email.contains(searchQuery, ignoreCase = true) || it.firstName.contains(searchQuery, ignoreCase = true) || it.lastName.contains(searchQuery, ignoreCase = true) }
            .map { it.toUser() }
    }

    override suspend fun userById(id: Int): User? {
        return users.firstOrNull { it.id == id }?.toUser()
    }

    override suspend fun authUserByEmail(email: String): AuthUser? {
        return users.firstOrNull { it.email == email }?.toAuthUser()
    }
}