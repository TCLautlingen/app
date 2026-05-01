package org.tcl.app.booking.data

import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.websocket.Frame
import io.ktor.websocket.readText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import org.tcl.app.booking.BookingWsMessage
import org.tcl.app.core.data.SecureStorage
import org.tcl.app.core.data.network.BACKEND_ORIGIN
import kotlin.time.Duration.Companion.milliseconds

private const val WS_PATH = "/v1/bookings/ws"
private const val INITIAL_RETRY_DELAY_MS = 1_000L
private const val MAX_RETRY_DELAY_MS = 60_000L

class BookingWebSocketDataSource(
    private val secureStorage: SecureStorage,
) {
    // Separate client with no Auth plugin to avoid interference with the token-refresh state machine.
    // The JWT is attached manually on each connection attempt.
    private val wsClient = HttpClient { install(WebSockets) }
    private val json = Json { ignoreUnknownKeys = true }

    // Scope lives for the lifetime of the Koin singleton — one connection for the whole app session.
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    private val _messages = MutableSharedFlow<BookingWsMessage>(extraBufferCapacity = 64)
    val messages: SharedFlow<BookingWsMessage> = _messages.asSharedFlow()

    private val _isConnected = MutableStateFlow(false)
    val isConnected: StateFlow<Boolean> = _isConnected.asStateFlow()

    init {
        scope.launch { connectLoop() }
    }

    private suspend fun connectLoop() {
        var attempts = 0
        while (true) {
            try {
                val token = secureStorage.tokens.accessToken
                val wsUrl = BACKEND_ORIGIN
                    .replace("https://", "wss://")
                    .replace("http://", "ws://") + WS_PATH

                // Browser WebSockets cannot set custom headers, so the token is passed as a
                // query parameter. The server accepts it from either the Authorization header
                // or the ?token= query parameter.
                wsClient.webSocket(urlString = "$wsUrl?token=$token") {
                    attempts = 0
                    _isConnected.value = true
                    for (frame in incoming) {
                        if (frame is Frame.Text) {
                            try {
                                _messages.emit(json.decodeFromString<BookingWsMessage>(frame.readText()))
                            } catch (_: Exception) {
                                // Malformed frame — ignore and continue receiving
                            }
                        }
                    }
                }
            } catch (_: Exception) {
                // Connection failed or dropped — fall through to backoff
            }

            _isConnected.value = false

            // Exponential backoff: 1s, 2s, 4s, 8s, 16s, 32s, capped at 60s
            val delayMs = minOf(MAX_RETRY_DELAY_MS, INITIAL_RETRY_DELAY_MS * (1L shl attempts))
            attempts = (attempts + 1).coerceAtMost(6)
            delay(delayMs.milliseconds)
        }
    }
}
