package org.tcl.app.routes

import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import org.tcl.app.auth.EMAIL_ALREADY_EXISTS_ERROR
import org.tcl.app.auth.LoginRequest
import org.tcl.app.auth.LogoutRequest
import org.tcl.app.auth.RefreshRequest
import org.tcl.app.auth.RegisterRequest
import org.tcl.app.auth.RegisterResult
import org.tcl.app.auth.VALIDATION_ERROR_EMAIL
import org.tcl.app.auth.VALIDATION_ERROR_PASSWORD
import org.tcl.app.services.UserService

fun Route.authRoutes() {
    val userService by inject<UserService>()

    route("/auth") {
        post("/register") {
            val request = call.receive<RegisterRequest>()
            require(request.email.matches(Regex(".+@.+\\..+"))) { VALIDATION_ERROR_EMAIL }
            require(request.password.length >= 8) { VALIDATION_ERROR_PASSWORD }

            val registerResult = userService.registerUser(
                email = request.email,
                password = request.password
            )

            when (registerResult) {
                is RegisterResult.EmailAlreadyExists -> call.respond(HttpStatusCode.Conflict, EMAIL_ALREADY_EXISTS_ERROR)
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
                refreshToken = request.refreshToken
            )
            if (!success) {
                return@post call.respond(HttpStatusCode.BadRequest)
            }
            call.respond(HttpStatusCode.OK)
        }
    }
}
