package org.tcl.app.repositories

import org.tcl.app.models.RefreshToken

interface RefreshTokenRepository {
    suspend fun addRefreshToken(userId: Int, refreshToken: String, expiresAt: Long)
    suspend fun tokenByToken(refreshToken: String): RefreshToken?
    suspend fun removeRefreshToken(refreshToken: String): Boolean
}
