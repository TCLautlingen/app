package org.tcl.app.auth.data

import org.tcl.app.AuthTokens
import org.tcl.app.auth.domain.AuthRepository
import org.tcl.app.auth.domain.LoginError
import org.tcl.app.auth.domain.RegisterError
import org.tcl.app.core.domain.util.DataError
import org.tcl.app.core.domain.util.EmptyResult
import org.tcl.app.core.domain.util.Result

class AuthRepositoryImpl(
    private val api: AuthApiService
) : AuthRepository {
    override suspend fun refresh(refreshToken: String) : Result<AuthTokens, DataError> = api.refresh(refreshToken)

    override suspend fun login(email: String, password: String) : Result<AuthTokens, LoginError> = api.login(email, password)

    override suspend fun logout(deviceUniqueId: String, refreshToken: String) : EmptyResult<DataError> =
        api.logout(deviceUniqueId, refreshToken)

    override suspend fun register(email: String, password: String, firstName: String, lastName: String) : Result<AuthTokens, RegisterError>
            = api.register(email, password, firstName, lastName)
}