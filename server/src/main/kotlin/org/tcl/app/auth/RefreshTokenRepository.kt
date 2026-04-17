package org.tcl.app.auth

interface RefreshTokenRepository {
    suspend fun addRefreshToken(userId: Int, refreshToken: String, expiresAt: Long)
    suspend fun tokenByToken(refreshToken: String): RefreshToken?
    suspend fun removeRefreshToken(refreshToken: String): Boolean
}