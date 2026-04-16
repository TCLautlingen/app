package org.tcl.app.model

import org.tcl.app.RefreshToken
import org.tcl.app.User

interface UserRepository {
    fun register(email: String, password: String, firstName: String, lastName: String): User?

    fun login(email: String, password: String): User?

    fun getUsers(): List<User>

    fun findById(id: String): User?

    fun saveRefreshToken(token: RefreshToken)

    fun findRefreshToken(token: String): RefreshToken?

    fun revokeRefreshToken(token: String)

    fun revokeAllRefreshTokens(userId: String)
}