package org.tcl.app.routes

import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.authenticate
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.patch
import io.ktor.server.routing.route
import org.koin.ktor.ext.inject
import org.tcl.app.JwtConfig.userId
import org.tcl.app.services.UserService
import org.tcl.app.user.AdminUpdateUserRequest
import org.tcl.app.user.UpdateUserRequest

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

                val user = userService.getDetailedUserById(userId)
                    ?: return@get call.respond(HttpStatusCode.NotFound)

                call.respond(user)
            }

            patch("/me") {
                val userId = call.userId()
                val request = call.receive<UpdateUserRequest>()

                val updatedUser = userService.updateUser(
                    userId = userId,
                    firstName = request.firstName?.trim(),
                    lastName = request.lastName?.trim(),
                    phoneNumber = request.phoneNumber,
                    address = request.address,
                ) ?: return@patch call.respond(HttpStatusCode.NotFound)

                call.respond(updatedUser)
            }

            get("/{id}") {
                val calledUserId = call.userId()

                val requestUser = userService.getAuthUserById(calledUserId)
                    ?: return@get call.respond(HttpStatusCode.NotFound)

                if (!requestUser.isAdmin) {
                    return@get call.respond(HttpStatusCode.Forbidden)
                }

                val id = requireNotNull(call.parameters["id"]?.toIntOrNull()) { "Invalid id" }

                val user = userService.getDetailedUserById(id)
                    ?: return@get call.respond(HttpStatusCode.NotFound)
                call.respond(user)
            }

            patch("/{id}") {
                val calledUserId = call.userId()

                val requestUser = userService.getAuthUserById(calledUserId)
                    ?: return@patch call.respond(HttpStatusCode.Unauthorized)

                if (!requestUser.isAdmin) {
                    return@patch call.respond(HttpStatusCode.Forbidden)
                }

                val id = requireNotNull(call.parameters["id"]?.toIntOrNull()) { "Invalid id" }
                val request = call.receive<AdminUpdateUserRequest>()

                val updatedUser = userService.adminUpdateUser(
                    userId = id,
                    isMember = request.isMember,
                    isAdmin = request.isAdmin,
                ) ?: return@patch call.respond(HttpStatusCode.NotFound)

                call.respond(updatedUser)
            }
        }
    }
}
