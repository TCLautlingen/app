package org.tcl.app.routes

import io.ktor.server.auth.authenticate
import io.ktor.server.routing.Route
import io.ktor.server.websocket.webSocket
import io.ktor.websocket.close
import org.koin.ktor.ext.inject
import org.slf4j.LoggerFactory
import org.tcl.app.JwtConfig.userId
import org.tcl.app.services.BookingWebSocketService

private val logger = LoggerFactory.getLogger("BookingWebSocketRoutes")

fun Route.bookingWebSocketRoutes() {
    val wsService by inject<BookingWebSocketService>()

    authenticate("auth-jwt") {
        webSocket("/bookings/ws") {
            val userId = call.userId()
            logger.info("WS client connected userId={}", userId)
            wsService.register(userId, this)
            try {
                for (frame in incoming) { /* drain; client sends nothing */ }
            } finally {
                wsService.unregister(userId, this)
                logger.info("WS client disconnected userId={}", userId)
                close()
            }
        }
    }
}
