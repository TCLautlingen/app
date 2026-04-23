package org.tcl.app.models

import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.core.dao.id.IntIdTable
import org.jetbrains.exposed.v1.dao.IntEntity
import org.jetbrains.exposed.v1.dao.IntEntityClass

object DeviceTable : IntIdTable("device") {
    val userId = reference("user_id", UserTable)
    val deviceUniqueId = varchar("device_unique_id", 256).uniqueIndex()
    val notificationToken = varchar("notification_token", 256)
}

class DeviceDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<DeviceDAO>(DeviceTable)

    var userId by DeviceTable.userId
    var deviceUniqueId by DeviceTable.deviceUniqueId
    var notificationToken by DeviceTable.notificationToken
}

fun daoToDevice(dao: DeviceDAO) = Device(
    userId = dao.userId.value,
    deviceUniqueId = dao.deviceUniqueId,
    notificationToken = dao.notificationToken,
)
