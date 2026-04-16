package org.tcl.app.auth.data

import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.HttpStatusCode
import org.tcl.app.AuthTokens
import org.tcl.app.LoginRequest
import org.tcl.app.RegisterRequest
import org.tcl.app.core.data.ApiClient

class AuthApiService(
    private val apiClient: ApiClient
) {
    suspend fun refresh(tokens: AuthTokens): AuthTokens? {
        val response = apiClient.client.post("/auth/refresh") {
            setBody(tokens)
        }

        return if (response.status == HttpStatusCode.OK) {
            response.body()
        } else {
            null
        }
    }

    suspend fun login(email: String, password: String): AuthTokens? {
        val loginRequest = LoginRequest(email, password)

        val response = apiClient.client.post("/auth/login") {
            setBody(loginRequest)
        }

        return if (response.status == HttpStatusCode.OK) {
            response.body()
        } else {
            null
        }
    }

    suspend fun register(email: String, password: String, firstName: String, lastName: String): AuthTokens? {
        val registerRequest = RegisterRequest(email, password, firstName, lastName)

        val response = apiClient.client.post("/auth/register") {
            setBody(registerRequest)
        }

        return if (response.status == HttpStatusCode.OK) {
            response.body()
        } else {
            null
        }
    }
}
