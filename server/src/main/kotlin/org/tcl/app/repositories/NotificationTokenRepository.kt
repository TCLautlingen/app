package org.tcl.app.repositories

interface NotificationTokenRepository {
    suspend fun registerToken(userId: Int, token: String)
    suspend fun getTokensForUser(userId: Int): List<String>
    suspend fun removeToken(userId: Int, token: String)
    suspend fun getAllTokens(): List<String>
}
