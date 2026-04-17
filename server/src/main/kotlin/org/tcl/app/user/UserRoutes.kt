package org.tcl.app.user

import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import org.tcl.app.security.JwtConfig.toAuthPrincipal

fun Route.userRoutes(
    userService: UserService
) {
    authenticate("auth-jwt") {
        route("/users") {
            get {
                val searchQuery = call.request.queryParameters["searchQuery"] ?: ""
                val users = userService.getAllUsers(searchQuery)
                call.respond(users)
            }

            get("/me") {
                val authPrincipal = call.principal<JWTPrincipal>()?.toAuthPrincipal()
                    ?: return@get call.respond(HttpStatusCode.Unauthorized)

                val user = userService.getUserById(authPrincipal.userId)
                    ?: return@get call.respond("No user with id $authPrincipal.userId")

                call.respond(user)
            }
        }
    }
}