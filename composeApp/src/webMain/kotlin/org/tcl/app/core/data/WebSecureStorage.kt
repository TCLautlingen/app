package org.tcl.app.core.data

import kotlinx.browser.localStorage
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.tcl.app.auth.AuthTokens

private const val KEY_AUTH_TOKENS = "auth_tokens"

class WebSecureStorage : SecureStorage {
    private val json = Json { ignoreUnknownKeys = true }

    override var tokens: AuthTokens
        get() = try {
            localStorage.getItem(KEY_AUTH_TOKENS)
                ?.let { json.decodeFromString<AuthTokens>(it) }
                ?: AuthTokens()
        } catch (_: Exception) {
            AuthTokens()
        }
        set(value) {
            try {
                localStorage.setItem(KEY_AUTH_TOKENS, json.encodeToString(value))
            } catch (_: Exception) {
                // localStorage unavailable (e.g. private browsing with storage blocked)
            }
        }

    override fun clearAuthTokens() {
        localStorage.removeItem(KEY_AUTH_TOKENS)
    }
}
