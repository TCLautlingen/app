package org.tcl.app.routes

import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.authenticate
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.patch
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import org.koin.ktor.ext.inject
import org.tcl.app.notification.RegisterNotificationTokenRequest
import org.tcl.app.notification.SendNotificationRequest
import org.tcl.app.JwtConfig.userId
import org.tcl.app.services.NotificationService

fun Route.notificationRoutes() {
    val notificationService by inject<NotificationService>()

    authenticate("auth-jwt") {
        route("/notifications") {
            post("/register") {
                val userId = call.userId()
                val request = call.receive<RegisterNotificationTokenRequest>()

                notificationService.registerNotificationToken(
                    userId = userId,
                    token = request.token
                )

                call.respond(HttpStatusCode.OK)
            }

            delete("/{token}") {
                val userId = call.userId()
                val token = requireNotNull(call.parameters["token"]) { "Invalid token" }
                notificationService.removeNotificationToken(
                    userId = userId,
                    token = token
                )
            }

            post("/send") {
                val userId = call.userId()
                val request = call.receive<SendNotificationRequest>()
                require(request.title.isNotBlank()) { "title must not be blank" }
                require(request.body.isNotBlank()) { "body must not be blank" }

                notificationService.sendToAll(request.title, request.body, userId)
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
