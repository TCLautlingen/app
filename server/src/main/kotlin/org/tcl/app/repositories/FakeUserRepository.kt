package org.tcl.app.repositories

import org.tcl.app.models.AuthUser
import org.tcl.app.user.User

class FakeUserRepository : UserRepository {
    private data class StoredUser(
        val id: Int,
        val email: String,
        val passwordHash: String,
        val passwordSalt: String,
        val isMember: Boolean,
        val isAdmin: Boolean,
    ) {
        fun toUser(): User = User(id, email, "", "", isMember, isAdmin)

        fun toAuthUser(): AuthUser = AuthUser(id, email, passwordHash, passwordSalt)
    }

    private var nextId = 1

    private val users = mutableListOf<StoredUser>()

    override suspend fun createUser(
        email: String,
        passwordHash: String,
        passwordSalt: String,
    ): User? {
        if (users.any { user -> email == user.email }) {
            return null
        }

        val id = nextId++
        val storedUser = StoredUser(id, email, passwordHash, passwordSalt,
            isMember = true,
            isAdmin = true,
        )
        users.add(storedUser)

        return storedUser.toUser()
    }

    override suspend fun allUsers(searchQuery: String): List<User> {
        return users
            .filter { it.email.contains(searchQuery, ignoreCase = true) }
            .map { it.toUser() }
    }

    override suspend fun userById(id: Int): User? {
        return users.firstOrNull { it.id == id }?.toUser()
    }

    override suspend fun authUserByEmail(email: String): AuthUser? {
        return users.firstOrNull { it.email == email }?.toAuthUser()
    }
}
