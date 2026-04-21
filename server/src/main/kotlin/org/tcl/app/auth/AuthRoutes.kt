package org.tcl.app.auth

import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import org.tcl.app.user.UserService

fun Route.authRoutes() {
    val userService by inject<UserService>()

    route("/auth") {
        post("/register") {
            val request = call.receive<RegisterRequest>()

            val registerResult = userService.registerUser(
                email = request.email,
                password = request.password,
                firstName = request.firstName,
                lastName = request.lastName
            )

            when (registerResult) {
                is RegisterResult.EmailAlreadyExists -> call.respond(
                    HttpStatusCode.Conflict,
                    EMAIL_ALREADY_EXISTS_ERROR
                )
                is RegisterResult.ValidationError -> call.respond(
                    HttpStatusCode.BadRequest,
                    registerResult.message
                )
                is RegisterResult.Success -> call.respond(registerResult.tokens)
            }
        }

        post("/login") {
            val request = call.receive<LoginRequest>()

            val authTokens = userService.login(
                email = request.email,
                password = request.password
            ) ?: return@post call.respond(HttpStatusCode.Unauthorized)

            call.respond(authTokens)
        }

        post("/refresh") {
            val request = call.receive<RefreshRequest>()

            val authTokens = userService.refreshTokens(request.refreshToken)
                ?: return@post call.respond(HttpStatusCode.Unauthorized)

            call.respond(authTokens)
        }

        post("/logout") {
            val request = call.receive<LogoutRequest>()
            val success = userService.logout(
                deviceUniqueId = request.deviceUniqueId,
                refreshToken = request.refreshToken
            )
            if (!success) {
                return@post call.respond(HttpStatusCode.BadRequest)
            }
            call.respond(HttpStatusCode.OK)
        }
    }
}