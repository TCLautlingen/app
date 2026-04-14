package org.tcl.app.model

object RefreshTokenStore {
    private val tokens = mutableSetOf<String>()

    fun save(token: String) {
        tokens.add(token)
    }

    fun isValid(token: String) = tokens.contains(token)

    fun revoke(token: String) {
        tokens.remove(token)
    }
}