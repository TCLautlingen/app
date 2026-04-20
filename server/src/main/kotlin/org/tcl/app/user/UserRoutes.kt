package org.tcl.app.user

import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import org.tcl.app.DeviceTokenRequest
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

            post("/device-token") {
                val authPrincipal = call.principal<JWTPrincipal>()?.toAuthPrincipal()
                    ?: return@post call.respond(HttpStatusCode.Unauthorized)

                val user = userService.getUserById(authPrincipal.userId)
                    ?: return@post call.respond("No user with id $authPrincipal.userId")

                val request = call.receive<DeviceTokenRequest>()
                println("Received device token for user ${user.id}: ${request.deviceToken}")
                call.respond(HttpStatusCode.OK)
            }

            get("/{id}") {
                val authPrincipal = call.principal<JWTPrincipal>()?.toAuthPrincipal()
                    ?: return@get call.respond(HttpStatusCode.Unauthorized)

                val requestUser = userService.getUserById(authPrincipal.userId)
                    ?: return@get call.respond("No user with id $authPrincipal.userId")

                if (!requestUser.isAdmin) {
                    return@get call.respond(HttpStatusCode.Forbidden)
                }

                val id = call.parameters["id"]?.toIntOrNull()
                    ?: return@get call.respond(HttpStatusCode.BadRequest)

                val user = userService.getUserById(id)
                    ?: return@get call.respond("No user with id $id")
                call.respond(user)
            }
        }
    }
}