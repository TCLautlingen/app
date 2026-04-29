package org.tcl.app.repositories.postgres

import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.core.like
import org.jetbrains.exposed.v1.core.or
import org.jetbrains.exposed.v1.exceptions.ExposedSQLException
import org.tcl.app.db.entities.ProfileEntity
import org.tcl.app.db.entities.UserEntity
import org.tcl.app.mappers.entityToAuthUser
import org.tcl.app.mappers.entityToUser
import org.tcl.app.models.AuthUser
import org.tcl.app.db.withTransaction
import org.tcl.app.db.tables.ProfilesTable
import org.tcl.app.db.tables.UsersTable
import org.tcl.app.user.User
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.tcl.app.mappers.entityToDetailedUser
import org.tcl.app.repositories.UserRepository
import org.tcl.app.user.DetailedUser
import java.sql.SQLException

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
            val sqlState = (e.cause as? SQLException)?.sqlState
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

    override suspend fun updateUser(
        id: Int,
        firstName: String?,
        lastName: String?,
        phoneNumber: String?,
        address: String?,
    ): DetailedUser? = withTransaction {
        val user = UserEntity.findById(id) ?: return@withTransaction null

        val hasProfileFields = firstName != null || lastName != null || phoneNumber != null || address != null
        if (hasProfileFields) {
            val profile = user.profile
            if (profile != null) {
                if (firstName != null) profile.firstName = firstName
                if (lastName != null) profile.lastName = lastName
                if (phoneNumber != null) profile.phoneNumber = phoneNumber
                if (address != null) profile.address = address
            } else {
                ProfileEntity.new {
                    this.firstName = firstName ?: ""
                    this.lastName = lastName ?: ""
                    this.phoneNumber = phoneNumber
                    this.address = address
                    this.user = user
                }
            }
        }

        entityToDetailedUser(user)
    }

    override suspend fun adminUpdateUser(
        id: Int,
        isMember: Boolean?,
        isAdmin: Boolean?,
    ): DetailedUser? = withTransaction {
        val user = UserEntity.findById(id) ?: return@withTransaction null

        if (isMember != null) user.isMember = isMember
        if (isAdmin != null) user.isAdmin = isAdmin

        entityToDetailedUser(user)
    }

    override suspend fun userById(id: Int): User? = withTransaction {
        UserEntity
            .findById(id)
            ?.let(::entityToUser)
    }

    override suspend fun detailedUserById(id: Int): DetailedUser? = withTransaction {
        UserEntity
            .findById(id)
            ?.let(::entityToDetailedUser)
    }

    override suspend fun authUserById(id: Int): AuthUser? = withTransaction {
        UserEntity
            .findById(id)
            ?.let(::entityToAuthUser)
    }

    override suspend fun authUserByEmail(email: String): AuthUser? = withTransaction {
        UserEntity
            .find { UsersTable.email eq email }
            .firstOrNull()
            ?.let(::entityToAuthUser)
    }
}
