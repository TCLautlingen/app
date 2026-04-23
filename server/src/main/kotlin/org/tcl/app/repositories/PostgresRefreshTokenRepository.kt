package org.tcl.app.repositories

import org.jetbrains.exposed.v1.core.eq
import org.tcl.app.models.RefreshToken
import org.tcl.app.models.RefreshTokenDAO
import org.tcl.app.models.RefreshTokenTable
import org.tcl.app.models.UserDAO
import org.tcl.app.models.daoToRefreshToken
import org.tcl.app.plugins.withTransaction

class PostgresRefreshTokenRepository : RefreshTokenRepository {
    override suspend fun addRefreshToken(
        userId: Int,
        refreshToken: String,
        expiresAt: Long
    ): Unit = withTransaction {
        RefreshTokenDAO.new {
            user = UserDAO[userId]
            token = refreshToken
            this.expiresAt = expiresAt
        }
    }

    override suspend fun tokenByToken(refreshToken: String): RefreshToken? = withTransaction {
        RefreshTokenDAO
            .find {
                RefreshTokenTable.token eq refreshToken
            }
            .limit(1)
            .firstOrNull()
            ?.let(::daoToRefreshToken)
    }

    override suspend fun removeRefreshToken(refreshToken: String): Boolean = withTransaction {
        RefreshTokenDAO
            .find {
                RefreshTokenTable.token eq refreshToken
            }
            .limit(1)
            .firstOrNull()
            ?.delete() != null
    }
}
