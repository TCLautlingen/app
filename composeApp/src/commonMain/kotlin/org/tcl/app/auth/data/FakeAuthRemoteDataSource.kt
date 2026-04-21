package org.tcl.app.auth.data

import org.tcl.app.AuthTokens
import org.tcl.app.auth.domain.AuthRemoteDataSource
import org.tcl.app.auth.domain.LoginError
import org.tcl.app.auth.domain.RegisterError
import org.tcl.app.core.domain.util.DataError
import org.tcl.app.core.domain.util.EmptyResult
import org.tcl.app.core.domain.util.Result

class FakeAuthRemoteDataSource : AuthRemoteDataSource {
    private val fakeTokens = AuthTokens(
        accessToken = "fake-access-token",
        refreshToken = "fake-refresh-token"
    )

    override suspend fun refresh(
        refreshToken: String
    ): Result<AuthTokens, DataError> = Result.Success(fakeTokens)

    override suspend fun login(
        email: String,
        password: String
    ): Result<AuthTokens, LoginError> {
        return if (password == "wrong") Result.Error(LoginError.InvalidCredentials)
        else Result.Success(fakeTokens)
    }

    override suspend fun logout(
        deviceUniqueId: String,
        refreshToken: String
    ): EmptyResult<DataError> = Result.Success(Unit)

    override suspend fun register(
        email: String,
        password: String,
        firstName: String,
        lastName: String
    ): Result<AuthTokens, RegisterError> {
        return if (email == "taken@test.com") Result.Error(RegisterError.EmailAlreadyExists)
        else Result.Success(fakeTokens)
    }
}