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
import org.koin.ktor.ext.inject
import org.tcl.app.notification.NotificationTokenRequest
import org.tcl.app.security.JwtConfig.userId

fun Route.userRoutes() {
    val userService by inject<UserService>()

    authenticate("auth-jwt") {
        route("/users") {
            get {
                val searchQuery = call.request.queryParameters["searchQuery"] ?: ""
                val users = userService.getAllUsers(searchQuery)
                call.respond(users)
            }

            get("/me") {
                val userId = call.userId()

                val user = userService.getUserById(userId)
                    ?: return@get call.respond("No user with id $userId")

                call.respond(user)
            }

            post("/notificationToken") {
                val userId = call.userId()

                val request = call.receive<NotificationTokenRequest>()
                userService.updateDevice(
                    userId = userId,
                    deviceUniqueId = request.deviceUniqueId,
                    notificationToken = request.notificationToken
                )
                call.respond(HttpStatusCode.OK)
            }

            get("/{id}") {
                val calledUserId = call.userId()

                val requestUser = userService.getUserById(calledUserId)
                    ?: return@get call.respond("No user with id $calledUserId")

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