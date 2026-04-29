package org.tcl.app.db.entities

import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.dao.IntEntity
import org.jetbrains.exposed.v1.dao.IntEntityClass
import org.tcl.app.db.tables.ProfilesTable

class ProfileEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<ProfileEntity>(ProfilesTable)

    var firstName by ProfilesTable.firstName
    var lastName by ProfilesTable.lastName
    var phoneNumber by ProfilesTable.phoneNumber
    var address by ProfilesTable.address
    var user by UserEntity referencedOn ProfilesTable.user
}