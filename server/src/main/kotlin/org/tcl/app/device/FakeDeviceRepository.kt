package org.tcl.app.device

class FakeDeviceRepository : DeviceRepository {
    private val devices = mutableListOf<Device>()

    override suspend fun upsertDevice(
        userId: Int,
        deviceUniqueId: String,
        notificationToken: String
    ) {
        val existingDevice = devices.find { it.userId == userId && it.deviceUniqueId == deviceUniqueId }
        if (existingDevice != null) {
            existingDevice.notificationToken = notificationToken
        } else {
            devices.add(Device(userId, deviceUniqueId, notificationToken))
        }
    }

    override suspend fun getTokensForUser(userId: Int): List<String> {
        return devices.filter { it.userId == userId }.map { it.notificationToken }
    }

    override suspend fun removeDevice(deviceUniqueId: String) {
        devices.removeAll { it.deviceUniqueId == deviceUniqueId }
    }

    override suspend fun getAllDevices(): List<Device> {
        return devices.toList()
    }
}