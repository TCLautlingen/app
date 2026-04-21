package org.tcl.app.auth.data

import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.HttpStatusCode
import org.tcl.app.auth.AuthTokens
import org.tcl.app.auth.LoginRequest
import org.tcl.app.auth.LogoutRequest
import org.tcl.app.auth.RefreshRequest
import org.tcl.app.auth.RegisterRequest
import org.tcl.app.auth.VALIDATION_ERROR_EMAIL
import org.tcl.app.auth.VALIDATION_ERROR_FIRST_NAME
import org.tcl.app.auth.VALIDATION_ERROR_LAST_NAME
import org.tcl.app.auth.VALIDATION_ERROR_PASSWORD
import org.tcl.app.auth.domain.AuthRemoteDataSource
import org.tcl.app.auth.domain.LoginError
import org.tcl.app.auth.domain.RegisterError
import org.tcl.app.core.data.ApiClient
import org.tcl.app.core.domain.util.DataError
import org.tcl.app.core.domain.util.EmptyResult
import org.tcl.app.core.domain.util.Result
import org.tcl.app.core.domain.util.safeApiCall

class KtorAuthRemoteDataSource(
    private val apiClient: ApiClient
) : AuthRemoteDataSource {
    override suspend fun refresh(refreshToken: String): Result<AuthTokens, DataError> = safeApiCall {
        apiClient.client.post("/auth/refresh") {
            setBody(RefreshRequest(refreshToken))
        }
    }

    override suspend fun login(email: String, password: String): Result<AuthTokens, LoginError> {
        val loginRequest = LoginRequest(email, password)

        val response = apiClient.client.post("/auth/login") {
            setBody(loginRequest)
        }

        return when (response.status) {
            HttpStatusCode.OK -> Result.Success(response.body<AuthTokens>())
            HttpStatusCode.Unauthorized -> Result.Error(LoginError.InvalidCredentials)
            else -> Result.Error(LoginError.Unknown)
        }
    }

    override suspend fun logout(deviceUniqueId: String, refreshToken: String): EmptyResult<DataError> = safeApiCall {
        apiClient.client.post("/auth/logout") {
            setBody(LogoutRequest(
                deviceUniqueId = deviceUniqueId,
                refreshToken = refreshToken
            ))
        }
    }

    override suspend fun register(email: String, password: String, firstName: String, lastName: String): Result<AuthTokens, RegisterError>  {
        val registerRequest = RegisterRequest(email, password, firstName, lastName)

        val response = apiClient.client.post("/auth/register") {
            setBody(registerRequest)
        }

        return when (response.status) {
            HttpStatusCode.OK -> Result.Success(response.body<AuthTokens>())
            HttpStatusCode.Conflict -> Result.Error(RegisterError.EmailAlreadyExists)
            HttpStatusCode.BadRequest -> {
                when (response.body<String>()) {
                    VALIDATION_ERROR_EMAIL -> Result.Error(RegisterError.InvalidEmail)
                    VALIDATION_ERROR_PASSWORD -> Result.Error(RegisterError.PasswordTooWeak)
                    VALIDATION_ERROR_FIRST_NAME -> Result.Error(RegisterError.FirstNameEmpty)
                    VALIDATION_ERROR_LAST_NAME -> Result.Error(RegisterError.LastNameEmpty)
                    else -> Result.Error(RegisterError.Unknown)
                }
            }
            else -> Result.Error(RegisterError.Unknown)
        }
    }
}
