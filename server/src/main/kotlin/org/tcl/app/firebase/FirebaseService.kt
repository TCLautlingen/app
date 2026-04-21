package org.tcl.app.firebase

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.MulticastMessage
import com.google.firebase.messaging.Notification

class FirebaseService {
    init {
        val serviceAccount = this::class.java
            .classLoader
            .getResourceAsStream("tc-lautlingen-ec846-firebase-adminsdk-fbsvc-3cbbbaa0b8.json")
            ?: throw IllegalStateException("Firebase config not found")

        val options = FirebaseOptions.builder()
            .setCredentials(GoogleCredentials.fromStream(serviceAccount))
            .build()

        FirebaseApp.initializeApp(options)
    }

    fun sendToTokens(tokens: List<String>, title: String, body: String, data: Map<String, String> = emptyMap()) {
        tokens.chunked(500).forEach { chunk ->
            val message = MulticastMessage.builder()
                .setNotification(
                    Notification.builder()
                        .setTitle(title)
                        .setBody(body)
                        .build()
                )
                .putAllData(data)
                .addAllTokens(chunk)
                .build()
            FirebaseMessaging.getInstance().sendEachForMulticast(message)
        }
    }
}