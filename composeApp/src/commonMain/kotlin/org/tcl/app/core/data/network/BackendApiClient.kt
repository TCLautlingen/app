package org.tcl.app.core.data.network

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import org.tcl.app.auth.AuthTokens
import org.tcl.app.auth.RefreshRequest
import org.tcl.app.core.data.SecureStorage

class BackendApiClient(
    private val secureStorage: SecureStorage
) {
    private val refreshClient = HttpClient {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }

        defaultRequest {
            url(BACKEND_BASE_URL)
            contentType(ContentType.Application.Json)
        }
    }

    val client = HttpClient {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }

        defaultRequest {
            url(BACKEND_BASE_URL)
            contentType(ContentType.Application.Json)
        }

        install(Auth) {
            bearer {
                loadTokens {
                    BearerTokens(
                        secureStorage.tokens.accessToken,
                        secureStorage.tokens.refreshToken
                    )
                }

                refreshTokens {
                    try {
                        val response: AuthTokens = refreshClient.post("auth/refresh") {
                            contentType(ContentType.Application.Json)
                            setBody(RefreshRequest(secureStorage.tokens.refreshToken))
                        }.body()

                        secureStorage.tokens = response

                        BearerTokens(response.accessToken, response.refreshToken)
                    } catch (e: Exception) {
                        secureStorage.clearAuthTokens()
                        null
                    }
                }
            }
        }
    }
}