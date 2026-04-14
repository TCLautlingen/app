package org.tcl.app.routes

import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import org.tcl.app.AuthTokens
import org.tcl.app.LoginRequest
import org.tcl.app.RegisterRequest
import org.tcl.app.model.RefreshTokenStore
import org.tcl.app.security.JwtConfig

fun Route.authRoutes() {
    route("/auth") {
        post("/register") {
            val request = call.receive<RegisterRequest>()

            val userId = "user123"

            val access = JwtConfig.generateAccessToken(userId)
            val refresh = JwtConfig.generateRefreshToken(userId)

            RefreshTokenStore.save(refresh)

            call.respond(AuthTokens(access, refresh))
        }

        post("/login") {
            val request = call.receive<LoginRequest>()

            val userId = "user123"

            val access = JwtConfig.generateAccessToken(userId)
            val refresh = JwtConfig.generateRefreshToken(userId)

            RefreshTokenStore.save(refresh)

            call.respond(AuthTokens(access, refresh))
        }

        post("/refresh") {
            val req = call.receive<AuthTokens>()

            if (!RefreshTokenStore.isValid(req.refreshToken)) {
                return@post call.respond(HttpStatusCode.Unauthorized)
            }

            val decoded = JwtConfig.verifyRefreshToken(req.refreshToken)

            if (decoded == null) {
                return@post call.respond(HttpStatusCode.Unauthorized)
            }

            val claim = decoded.getClaim("userId")
            if (claim.isNull) {
                return@post call.respond(HttpStatusCode.Unauthorized)
            }

            val userId = claim.asString()

            val newAccess = JwtConfig.generateAccessToken(userId)
            val newRefresh = JwtConfig.generateRefreshToken(userId)

            RefreshTokenStore.revoke(req.refreshToken)
            RefreshTokenStore.save(newRefresh)

            call.respond(AuthTokens(newAccess, newRefresh))
        }
    }
}