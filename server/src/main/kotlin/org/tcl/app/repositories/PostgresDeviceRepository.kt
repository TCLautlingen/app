package org.tcl.app.repositories

import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.eq
import org.tcl.app.models.Device
import org.tcl.app.models.DeviceDAO
import org.tcl.app.models.DeviceTable
import org.tcl.app.models.UserDAO
import org.tcl.app.models.daoToDevice
import org.tcl.app.plugins.withTransaction

class PostgresDeviceRepository : DeviceRepository {
    override suspend fun upsertDevice(
        userId: Int,
        deviceUniqueId: String,
        notificationToken: String
    ): Unit = withTransaction {
        val existing = DeviceDAO.find { DeviceTable.deviceUniqueId eq deviceUniqueId }.firstOrNull()
        if (existing != null) {
            existing.notificationToken = notificationToken
        } else {
            DeviceDAO.new {
                this.userId = UserDAO[userId].id
                this.deviceUniqueId = deviceUniqueId
                this.notificationToken = notificationToken
            }
        }
    }

    override suspend fun getTokensForUser(userId: Int): List<String> = withTransaction {
        DeviceDAO.find { DeviceTable.userId eq userId }.map { it.notificationToken }
    }

    override suspend fun removeDevice(deviceUniqueId: String): Unit = withTransaction {
        DeviceDAO
            .find { (DeviceTable.deviceUniqueId eq deviceUniqueId) }
            .forEach { it.delete() }
    }

    override suspend fun getAllDevices(): List<Device> = withTransaction {
        DeviceDAO.all()
            .map(::daoToDevice)
    }
}
