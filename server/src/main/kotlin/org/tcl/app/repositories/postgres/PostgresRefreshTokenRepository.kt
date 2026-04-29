package org.tcl.app.repositories.postgres

import org.jetbrains.exposed.v1.core.eq
import org.tcl.app.db.entities.RefreshTokenEntity
import org.tcl.app.db.entities.UserEntity
import org.tcl.app.mappers.entityToRefreshToken
import org.tcl.app.models.RefreshToken
import org.tcl.app.db.withTransaction
import org.tcl.app.repositories.RefreshTokenRepository
import org.tcl.app.db.tables.RefreshTokensTable

class PostgresRefreshTokenRepository : RefreshTokenRepository {
    override suspend fun addRefreshToken(
        userId: Int,
        refreshToken: String,
        expiresAt: Long
    ): Unit = withTransaction {
        RefreshTokenEntity.new {
            user = UserEntity[userId]
            token = refreshToken
            this.expiresAt = expiresAt
        }
    }

    override suspend fun tokenByToken(refreshToken: String): RefreshToken? = withTransaction {
        RefreshTokenEntity
            .find {
                RefreshTokensTable.token eq refreshToken
            }
            .limit(1)
            .firstOrNull()
            ?.let(::entityToRefreshToken)
    }

    override suspend fun removeRefreshToken(refreshToken: String): Boolean = withTransaction {
        RefreshTokenEntity
            .find {
                RefreshTokensTable.token eq refreshToken
            }
            .limit(1)
            .firstOrNull()
            ?.delete() != null
    }
}
