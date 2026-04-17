package org.tcl.app.auth.domain

import org.tcl.app.AuthTokens
import org.tcl.app.auth.data.AuthApiService
import org.tcl.app.core.domain.util.Result

class AuthRepository(
    private val api: AuthApiService
) {
    suspend fun refresh(refreshToken: String) : AuthTokens? = api.refresh(refreshToken)

    suspend fun login(email: String, password: String) : AuthTokens? = api.login(email, password)

    suspend fun register(email: String, password: String, firstName: String, lastName: String) : Result<AuthTokens, RegisterError>
        = api.register(email, password, firstName, lastName)
}