package org.tcl.app.auth.domain

import org.tcl.app.AuthTokens
import org.tcl.app.core.domain.util.DataError
import org.tcl.app.core.domain.util.EmptyResult
import org.tcl.app.core.domain.util.Result

interface AuthRepository {
    suspend fun refresh(refreshToken: String) : Result<AuthTokens, DataError>
    suspend fun login(email: String, password: String) : Result<AuthTokens, LoginError>
    suspend fun logout(deviceUniqueId: String, refreshToken: String) : EmptyResult<DataError>
    suspend fun register(email: String, password: String, firstName: String, lastName: String) : Result<AuthTokens, RegisterError>
}