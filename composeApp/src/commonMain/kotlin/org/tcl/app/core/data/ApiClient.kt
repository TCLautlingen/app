package org.tcl.app.core.data

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.tcl.app.AuthTokens
import org.tcl.app.SERVER_PORT

class ApiClient(
    private val tokenManager: TokenManager,
    baseUrl: String = "http://localhost:$SERVER_PORT"
) {

    val client = HttpClient {

        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }

        defaultRequest {
            url(baseUrl)
            contentType(ContentType.Application.Json)
        }

        install(Auth) {
            bearer {
                loadTokens {
                    BearerTokens(
                        tokenManager.tokens.accessToken,
                        tokenManager.tokens.refreshToken
                    )
                }

                refreshTokens {
                    try {
                        val response: AuthTokens = client.post("/auth/refresh") {
                            setBody(tokenManager.tokens)
                        }.body()

                        tokenManager.tokens = response

                        BearerTokens(response.accessToken, response.refreshToken)
                    } catch (e: Exception) {
                        tokenManager.clear()
                        null
                    }
                }
            }
        }
    }
}