package org.tcl.app.device

interface DeviceRepository {
    suspend fun upsertDevice(userId: Int, deviceUniqueId: String, notificationToken: String)
    suspend fun getTokensForUser(userId: Int): List<String>
    suspend fun removeDevice(deviceUniqueId: String)
}