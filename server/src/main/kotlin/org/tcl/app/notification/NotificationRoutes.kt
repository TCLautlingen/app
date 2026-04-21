package org.tcl.app.notification

import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.patch
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import org.koin.ktor.ext.inject
import org.tcl.app.security.JwtConfig.toAuthPrincipal
import org.tcl.app.user.UserService

fun Route.notificationRoutes() {
    val notificationService by inject<NotificationService>()

    authenticate("auth-jwt") {
        route("/notifications") {
            post("/send") {
                val authPrincipal = call.principal<JWTPrincipal>()?.toAuthPrincipal()
                    ?: return@post call.respond(HttpStatusCode.Unauthorized)

                val request = call.receive<SendNotificationRequest>()
                notificationService.sendToAll(request.title, request.body, authPrincipal.userId)
                call.respond(HttpStatusCode.OK)
            }

            get("/inbox") {
                /*
                val authPrincipal = call.principal<JWTPrincipal>()?.toAuthPrincipal()
                    ?: return@get call.respond(HttpStatusCode.Unauthorized)

                val inbox = notificationService.getInbox(userId)
                call.respond(inbox)
                */
            }

            patch("/{id}/read") {
                /*
                val authPrincipal = call.principal<JWTPrincipal>()?.toAuthPrincipal()
                    ?: return@patch call.respond(HttpStatusCode.Unauthorized)

                val notificationId = call.parameters["id"]!!.toInt()
                notificationService.markAsRead(userId, notificationId)
                call.respond(HttpStatusCode.OK)
                 */
            }

            // Unread count (for badge on inbox tab)
            get("/unread-count") {
                /*
                val userId = call.principal<UserPrincipal>()?.id ?: return@post call.respond(HttpStatusCode.Unauthorized)
                val count = notificationService.getUnreadCount(userId)
                call.respond(mapOf("count" to count))

                 */
            }
        }
    }
}