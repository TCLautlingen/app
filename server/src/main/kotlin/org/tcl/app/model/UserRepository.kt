package org.tcl.app.model

import org.tcl.app.User

interface UserRepository {
    fun register(username: String, password: String): User?
    fun login(username: String, password: String): User?
    fun findById(id: String): User?
}