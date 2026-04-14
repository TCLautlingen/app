package org.tcl.app.auth.data

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.HttpStatusCode
import org.tcl.app.AuthTokens
import org.tcl.app.LoginRequest

class AuthApiService(
    private val client: HttpClient
) {
    suspend fun refresh(tokens: AuthTokens): AuthTokens? {
        val response = client.post("/auth/refresh") {
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

        val response = client.post("/auth/login") {
            setBody(loginRequest)
        }

        return if (response.status == HttpStatusCode.OK) {
            response.body()
        } else {
            null
        }
    }

    suspend fun register(email: String, password: String): AuthTokens? {
        val registerRequest = LoginRequest(email, password)

        val response = client.post("/auth/register") {
            setBody(registerRequest)
        }

        return if (response.status == HttpStatusCode.OK) {
            response.body()
        } else {
            null
        }
    }
}
