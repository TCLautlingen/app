package org.tcl.app.model

import org.tcl.app.RefreshToken
import org.tcl.app.User
import java.util.UUID

class FakeUserRepository : UserRepository {
    private data class StoredUser(
        val id: String,
        val email: String,
        val password: String
    )

    private val users = mutableListOf<StoredUser>()
    private val refreshTokens = mutableListOf<RefreshToken>()

    override fun register(email: String, password: String): User? {
        if (users.any { it.email == email }) return null

        val id = UUID.randomUUID().toString()
        users.add(StoredUser(id, email, password))

        return User(id, email)
    }

    override fun login(email: String, password: String): User? {
        val user = users.firstOrNull {
            it.email == email && it.password == password
        }

        return user?.let { User(it.id, it.email) }
    }

    override fun findById(id: String): User? {
        return users.firstOrNull { it.id == id }
            ?.let { User(it.id, it.email) }
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