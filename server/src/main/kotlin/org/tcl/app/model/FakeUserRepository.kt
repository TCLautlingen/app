package org.tcl.app.model

import org.tcl.app.RefreshToken
import org.tcl.app.User
import java.util.UUID

class FakeUserRepository : UserRepository {
    private data class StoredUser(
        val id: String,
        val email: String,
        val password: String,
        val firstName: String,
        val lastName: String,
        val isAdmin: Boolean,
        val isMember: Boolean
    ) {
        fun toUser(): User = User(id, email, firstName, lastName, isAdmin, isMember)
    }

    private val users = mutableListOf<StoredUser>()
    private val refreshTokens = mutableListOf<RefreshToken>()

    override fun register(email: String, password: String, firstName: String, lastName: String): User? {
        if (users.any { it.email == email }) return null

        val id = UUID.randomUUID().toString()
        val storedUser = StoredUser(id, email, password, firstName, lastName,
            isAdmin = true,
            isMember = true
        )
        users.add(storedUser)

        return storedUser.toUser()
    }

    override fun login(email: String, password: String): User? {
        val user = users.firstOrNull {
            it.email == email && it.password == password
        }

        return user?.toUser()
    }

    override fun getUsers(): List<User> {
        return users
            .map { it.toUser() }
    }

    override fun findById(id: String): User? {
        return users.firstOrNull { it.id == id }?.toUser()
    }

    override fun saveRefreshToken(token: RefreshToken) {
        refreshTokens.add(token)
    }

    override fun findRefreshToken(token: String): RefreshToken? {
        return refreshTokens.firstOrNull { it.token == token }
    }

    override fun revokeRefreshToken(token: String) {
        refreshTokens.removeIf { it.token == token }
    }

    override fun revokeAllRefreshTokens(userId: String) {
        refreshTokens.removeIf { it.userId == userId }
    }
}