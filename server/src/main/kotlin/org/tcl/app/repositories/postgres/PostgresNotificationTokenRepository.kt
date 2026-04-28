package org.tcl.app.repositories.postgres

import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.eq
import org.tcl.app.entities.NotificationTokenEntity
import org.tcl.app.entities.UserEntity
import org.tcl.app.plugins.withTransaction
import org.tcl.app.repositories.NotificationTokenRepository
import org.tcl.app.tables.NotificationTokensTable

class PostgresNotificationTokenRepository : NotificationTokenRepository {

    override suspend fun registerToken(userId: Int, token: String): Unit = withTransaction {
        NotificationTokenEntity.new {
            this.user = UserEntity[userId]
            this.token = token
        }
    }

    override suspend fun getTokensForUser(userId: Int): List<String> = withTransaction {
        NotificationTokenEntity.find { NotificationTokensTable.user eq userId }.map { it.token }
    }

    override suspend fun removeToken(userId: Int, token: String) = withTransaction {
        NotificationTokenEntity
            .find { (NotificationTokensTable.user eq userId) and (NotificationTokensTable.token eq token) }
            .forEach { it.delete() }
    }

    override suspend fun getAllTokens(): List<String> = withTransaction {
        NotificationTokenEntity
            .all()
            .map { it.token }
    }
}
