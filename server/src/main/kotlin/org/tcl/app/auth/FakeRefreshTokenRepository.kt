package org.tcl.app.auth

class FakeRefreshTokenRepository() : RefreshTokenRepository {
    private val tokens = mutableListOf<RefreshToken>()

    override suspend fun addRefreshToken(userId: Int, refreshToken: String, expiresAt: Long) {
        tokens.add(RefreshToken(refreshToken, userId, expiresAt))
    }

    override suspend fun tokenByToken(refreshToken: String): RefreshToken? {
        return tokens.find { it.token == refreshToken }
    }

    override suspend fun removeRefreshToken(refreshToken: String): Boolean {
        return tokens.removeIf { it.token == refreshToken }
    }
}