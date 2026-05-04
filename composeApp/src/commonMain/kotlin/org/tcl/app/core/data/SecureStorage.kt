package org.tcl.app.core.data

import org.tcl.app.auth.AuthTokens

interface SecureStorage {
    var tokens: AuthTokens
    fun clearAuthTokens()
}
