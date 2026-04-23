package org.tcl.app.repositories

import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.core.like
import org.jetbrains.exposed.v1.core.or
import org.jetbrains.exposed.v1.exceptions.ExposedSQLException
import org.tcl.app.models.AuthUser
import org.tcl.app.models.UserDAO
import org.tcl.app.models.UserTable
import org.tcl.app.models.daoToAuthUser
import org.tcl.app.models.daoToUser
import org.tcl.app.plugins.withTransaction
import org.tcl.app.user.User

class PostgresUserRepository : UserRepository {
    override suspend fun createUser(
        email: String,
        passwordHash: String,
        passwordSalt: String,
        firstName: String,
        lastName: String
    ): User? = withTransaction {
        try {
            UserDAO.new {
                this.email = email
                this.passwordHash = passwordHash
                this.passwordSalt = passwordSalt
                this.firstName = firstName
                this.lastName = lastName
            }.let(::daoToUser)
        } catch (e: ExposedSQLException) {
            val sqlState = (e.cause as? java.sql.SQLException)?.sqlState
            if (sqlState == "23505") null else throw e
        }
    }

    override suspend fun allUsers(searchQuery: String): List<User> = withTransaction {
        UserDAO
            .find {
                UserTable.email like "%$searchQuery%" or (UserTable.firstName like "%$searchQuery%") or (UserTable.lastName like "%$searchQuery%")
            }
            .map(::daoToUser)
    }

    override suspend fun userById(id: Int): User? = withTransaction {
        UserDAO.findById(id)?.let(::daoToUser)
    }

    override suspend fun authUserByEmail(email: String): AuthUser? = withTransaction {
        UserDAO.find { UserTable.email eq email }.firstOrNull()?.let(::daoToAuthUser)
    }
}
