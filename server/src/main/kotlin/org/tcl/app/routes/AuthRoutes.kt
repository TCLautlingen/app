package org.tcl.app.routes

import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import org.tcl.app.AuthTokens
import org.tcl.app.LoginRequest
import org.tcl.app.RefreshToken
import org.tcl.app.RegisterRequest
import org.tcl.app.model.UserRepository
import org.tcl.app.security.JwtConfig

fun Route.authRoutes(
    repository: UserRepository
) {
    route("/auth") {
        post("/register") {
            val request = call.receive<RegisterRequest>()

            val user = repository.register(request.email, request.password)
                ?: return@post call.respond(HttpStatusCode.Conflict)

            val access = JwtConfig.generateAccessToken(user.id)
            val refresh = JwtConfig.generateRefreshToken(user.id)

            repository.saveRefreshToken(
                RefreshToken(
                    token = refresh,
                    userId = user.id,
                    expiresAt = System.currentTimeMillis() + 7L * 24 * 60 * 60 * 1000
                )
            )

            call.respond(AuthTokens(access, refresh))
        }

        post("/login") {
            val request = call.receive<LoginRequest>()

            val user = repository.login(request.email, request.password)
                ?: return@post call.respond(HttpStatusCode.Unauthorized)

            val access = JwtConfig.generateAccessToken(user.id)
            val refresh = JwtConfig.generateRefreshToken(user.id)

            repository.saveRefreshToken(
                RefreshToken(
                    token = refresh,
                    userId = user.id,
                    expiresAt = System.currentTimeMillis() + 7L * 24 * 60 * 60 * 1000
                )
            )

            call.respond(AuthTokens(access, refresh))
        }

        post("/refresh") {
            val request = call.receive<AuthTokens>()

            val stored = repository.findRefreshToken(request.refreshToken)
                ?: return@post call.respond(HttpStatusCode.Unauthorized)

            if (stored.expiresAt < System.currentTimeMillis()) {
                repository.revokeRefreshToken(request.refreshToken)
                return@post call.respond(HttpStatusCode.Unauthorized)
            }

            val userId = stored.userId

            val newAccess = JwtConfig.generateAccessToken(userId)
            val newRefresh = JwtConfig.generateRefreshToken(userId)

            repository.revokeRefreshToken(request.refreshToken)

            repository.saveRefreshToken(
                RefreshToken(
                    token = newRefresh,
                    userId = userId,
                    expiresAt = System.currentTimeMillis() + 7L * 24 * 60 * 60 * 1000
                )
            )

            call.respond(AuthTokens(newAccess, newRefresh))
        }
    }
}