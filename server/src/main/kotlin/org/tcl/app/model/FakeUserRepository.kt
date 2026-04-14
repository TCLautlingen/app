package org.tcl.app.model

import org.tcl.app.User
import java.util.UUID

class FakeUserRepository : UserRepository {
    private data class StoredUser(val id: String, val username: String, val password: String)

    private val users = mutableListOf<StoredUser>()

    override fun register(username: String, password: String): User? {
        if (users.any { it.username == username }) return null
        val id = UUID.randomUUID().toString()
        val stored = StoredUser(id, username, password)
        users.add(stored)
        return User(id, username)
    }

    override fun login(username: String, password: String): User? {
        val stored = users.firstOrNull { it.username == username && it.password == password }
        return stored?.let { User(it.id, it.username) }
    }

    override fun findById(id: String): User? {
        return users.firstOrNull { it.id == id }?.let { User(it.id, it.username) }
    }
}