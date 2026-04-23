package org.tcl.app.repositories

import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.core.like
import org.jetbrains.exposed.v1.core.or
import org.jetbrains.exposed.v1.exceptions.ExposedSQLException
import org.tcl.app.entities.UserEntity
import org.tcl.app.mappers.entityToAuthUser
import org.tcl.app.mappers.entityToUser
import org.tcl.app.models.AuthUser
import org.tcl.app.plugins.withTransaction
import org.tcl.app.tables.ProfilesTable
import org.tcl.app.tables.UsersTable
import org.tcl.app.user.User
import org.jetbrains.exposed.v1.jdbc.selectAll

class PostgresUserRepository : UserRepository {
    override suspend fun createUser(
        email: String,
        passwordHash: String,
        passwordSalt: String,
    ): User? = withTransaction {
        try {
            UserEntity.new {
                this.email = email
                this.passwordHash = passwordHash
                this.passwordSalt = passwordSalt
            }.let(::entityToUser)
        } catch (e: ExposedSQLException) {
            val sqlState = (e.cause as? java.sql.SQLException)?.sqlState
            if (sqlState == "23505") null else throw e
        }
    }

    override suspend fun allUsers(searchQuery: String): List<User> = withTransaction {
        val pattern = "%$searchQuery%"
        UserEntity.wrapRows(
            (UsersTable leftJoin ProfilesTable)
                .selectAll()
                .where {
                    UsersTable.email like pattern or
                    (ProfilesTable.firstName like pattern) or
                    (ProfilesTable.lastName like pattern)
                }
                .withDistinct()
        ).map(::entityToUser)
    }

    override suspend fun userById(id: Int): User? = withTransaction {
        UserEntity
            .findById(id)
            ?.let(::entityToUser)
    }

    override suspend fun authUserByEmail(email: String): AuthUser? = withTransaction {
        UserEntity
            .find { UsersTable.email eq email }
            .firstOrNull()
            ?.let(::entityToAuthUser)
    }
}
