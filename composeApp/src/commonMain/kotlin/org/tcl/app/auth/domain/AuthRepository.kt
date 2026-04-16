package org.tcl.app.auth.domain

import org.tcl.app.AuthTokens
import org.tcl.app.auth.data.AuthApiService

class AuthRepository(
    private val api: AuthApiService
) {
    suspend fun refresh(tokens: AuthTokens) : AuthTokens? = api.refresh(tokens)

    suspend fun login(email: String, password: String) : AuthTokens? = api.login(email, password)

    suspend fun register(email: String, password: String, firstName: String, lastName: String) : AuthTokens?
        = api.register(email, password, firstName, lastName)
}