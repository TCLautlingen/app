package org.tcl.app.user.domain

import org.tcl.app.User
import org.tcl.app.user.data.UserApiService

class UserRepository(
    private val api: UserApiService
) {
    suspend fun getUsers(searchQuery: String): List<User> = api.getUsers(searchQuery)

    suspend fun getCurrentUser(): User = api.getCurrentUser()
}