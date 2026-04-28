package org.tcl.app.auth.domain

import org.tcl.app.auth.AuthTokens
import org.tcl.app.core.domain.util.DataError
import org.tcl.app.core.domain.util.EmptyResult
import org.tcl.app.core.domain.util.Result

interface AuthRemoteDataSource {
    suspend fun refresh(refreshToken: String) : Result<AuthTokens, DataError>
    suspend fun login(email: String, password: String) : Result<AuthTokens, LoginError>
    suspend fun logout(refreshToken: String) : EmptyResult<DataError>
    suspend fun register(email: String, password: String) : Result<AuthTokens, RegisterErrors>
}