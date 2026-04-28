package org.tcl.app.repositories.fake

import org.tcl.app.repositories.NotificationTokenRepository


class FakeNotificationTokenRepository : NotificationTokenRepository {
    data class StoredToken(
        val userId: Int,
        val notificationToken: String,
    )
    private val tokens = mutableListOf<StoredToken>()

    override suspend fun registerToken(userId: Int, token: String) {
        tokens.add(StoredToken(userId, token))
    }

    override suspend fun getTokensForUser(userId: Int): List<String> {
        return tokens.filter { it.userId == userId }.map { it.notificationToken }
    }

    override suspend fun removeToken(userId: Int, token: String) {
        tokens.removeAll { it.userId == userId && it.notificationToken == token }
    }

    override suspend fun getAllTokens(): List<String> {
        return tokens.map { it.notificationToken }
    }
}
