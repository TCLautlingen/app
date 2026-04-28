package org.tcl.app.auth.data

import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.HttpStatusCode
import org.tcl.app.auth.AuthTokens
import org.tcl.app.auth.LoginRequest
import org.tcl.app.auth.LogoutRequest
import org.tcl.app.auth.RefreshRequest
import org.tcl.app.auth.RegisterError
import org.tcl.app.auth.RegisterRequest
import org.tcl.app.auth.domain.AuthRemoteDataSource
import org.tcl.app.auth.domain.LoginError
import org.tcl.app.auth.domain.RegisterErrors
import org.tcl.app.core.data.network.BackendApiClient
import org.tcl.app.core.domain.util.DataError
import org.tcl.app.core.domain.util.EmptyResult
import org.tcl.app.core.domain.util.Result
import org.tcl.app.core.domain.util.safeApiCall

class KtorAuthRemoteDataSource(
    private val backendApiClient: BackendApiClient
) : AuthRemoteDataSource {
    override suspend fun refresh(refreshToken: String): Result<AuthTokens, DataError> = safeApiCall {
        backendApiClient.client.post("auth/refresh") {
            setBody(RefreshRequest(refreshToken))
        }
    }

    override suspend fun login(email: String, password: String): Result<AuthTokens, LoginError> {
        val loginRequest = LoginRequest(email, password)

        val response = backendApiClient.client.post("auth/login") {
            setBody(loginRequest)
        }

        return when (response.status) {
            HttpStatusCode.OK -> Result.Success(response.body<AuthTokens>())
            HttpStatusCode.Unauthorized -> Result.Error(LoginError.InvalidCredentials)
            else -> Result.Error(LoginError.Unknown)
        }
    }

    override suspend fun logout(refreshToken: String): EmptyResult<DataError> = safeApiCall {
        backendApiClient.client.post("auth/logout") {
            setBody(LogoutRequest(
                refreshToken = refreshToken
            ))
        }
    }

    override suspend fun register(email: String, password: String): Result<AuthTokens, RegisterErrors>  {
        val registerRequest = RegisterRequest(email, password)

        val response = backendApiClient.client.post("auth/register") {
            setBody(registerRequest)
        }

        return when (response.status) {
            HttpStatusCode.OK -> Result.Success(response.body<AuthTokens>())
            HttpStatusCode.BadRequest -> Result.Error(RegisterErrors(response.body<List<RegisterError>>()))
            else -> Result.Error(RegisterErrors(emptyList()))
        }
    }
}
