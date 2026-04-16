package org.tcl.app.routes

import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import org.tcl.app.model.UserRepository

fun Route.userRoutes(
    repository: UserRepository
) {
    authenticate("auth-jwt") {
        route("/users") {
            get {
                val searchQuery = call.request.queryParameters["searchQuery"]
                val users = repository.getUsers()
                val filtered = searchQuery?.let {
                    users.filter { u ->
                        u.firstName.contains(it, true) ||
                                u.lastName.contains(it, true) ||
                                u.email.contains(it, true)
                    }
                } ?: users

                call.respond(filtered)
            }

            get("/me") {
                val principal = call.principal<JWTPrincipal>()
                val userId = principal?.payload?.getClaim("userId")?.asString()

                if (userId == null) {
                    return@get call.respond(HttpStatusCode.Unauthorized)
                }

                val user = repository.findById(userId)
                    ?: return@get call.respond("No user with id $userId")

                call.respond(user)
            }
        }
    }
}