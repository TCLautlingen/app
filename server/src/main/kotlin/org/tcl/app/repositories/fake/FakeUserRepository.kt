package org.tcl.app.repositories.fake

import org.tcl.app.models.AuthUser
import org.tcl.app.repositories.UserRepository
import org.tcl.app.user.DetailedUser
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
        fun toUser(): User = User(id, email, "", "")

        fun toDetailedUser(): DetailedUser = DetailedUser(id, email, "", "", isAdmin, isMember)

        fun toAuthUser(): AuthUser = AuthUser(id, email, passwordHash, passwordSalt, isAdmin)
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

    override suspend fun updateUser(
        id: Int,
        firstName: String?,
        lastName: String?,
        phoneNumber: String?,
        address: String?,
        isMember: Boolean?,
    ): DetailedUser? {
        val index = users.indexOfFirst { it.id == id }.takeIf { it >= 0 } ?: return null
        val current = users[index]
        val updated = current.copy(
            isMember = isMember ?: current.isMember,
        )
        users[index] = updated
        return updated.toDetailedUser()
    }

    override suspend fun allUsers(searchQuery: String): List<User> {
        return users
            .filter { it.email.contains(searchQuery, ignoreCase = true) }
            .map { it.toUser() }
    }

    override suspend fun userById(id: Int): User? {
        return users.firstOrNull { it.id == id }?.toUser()
    }

    override suspend fun detailedUserById(id: Int): DetailedUser? {
        return users.firstOrNull { it.id == id }?.toDetailedUser()
    }

    override suspend fun authUserById(id: Int): AuthUser? {
        return users.firstOrNull { it.id == id }?.toAuthUser()
    }

    override suspend fun authUserByEmail(email: String): AuthUser? {
        return users.firstOrNull { it.email == email }?.toAuthUser()
    }
}
