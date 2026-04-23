package org.tcl.app.repositories

import org.tcl.app.models.Device

interface DeviceRepository {
    suspend fun upsertDevice(userId: Int, deviceUniqueId: String, notificationToken: String)
    suspend fun getTokensForUser(userId: Int): List<String>
    suspend fun removeDevice(deviceUniqueId: String)
    suspend fun getAllDevices(): List<Device>
}
