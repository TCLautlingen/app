package org.tcl.app.services

import io.ktor.websocket.DefaultWebSocketSession
import io.ktor.websocket.Frame
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.slf4j.LoggerFactory
import org.tcl.app.booking.BookingWsMessage
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CopyOnWriteArraySet

class BookingWebSocketService {
    private val logger = LoggerFactory.getLogger(BookingWebSocketService::class.java)
    private val sessions = ConcurrentHashMap<Int, CopyOnWriteArraySet<DefaultWebSocketSession>>()
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val json = Json

    fun register(userId: Int, session: DefaultWebSocketSession) {
        sessions.getOrPut(userId) { CopyOnWriteArraySet() }.add(session)
        logger.info("WS registered userId={} totalSessions={}", userId, sessions[userId]?.size)
    }

    fun unregister(userId: Int, session: DefaultWebSocketSession) {
        sessions[userId]?.let { set ->
            val removed = set.remove(session)
            if (set.isEmpty()) sessions.remove(userId)
            if (removed) logger.info("WS unregistered userId={} remainingSessions={}", userId, set.size)
        }
    }

    fun notifyUsers(userIds: List<Int>, message: BookingWsMessage) {
        // Encode JSON once; create a fresh Frame per send to avoid shared-buffer corruption
        val text = json.encodeToString(message)
        logger.debug("WS notify userIds={} messageType={}", userIds, message::class.simpleName)
        for (userId in userIds) {
            val userSessions = sessions[userId] ?: continue
            scope.launch {
                for (session in userSessions) {
                    try {
                        session.send(Frame.Text(text))
                    } catch (e: Exception) {
                        logger.warn("WS send failed userId={}: {}", userId, e.message)
                        unregister(userId, session)
                    }
                }
            }
        }
    }
}
