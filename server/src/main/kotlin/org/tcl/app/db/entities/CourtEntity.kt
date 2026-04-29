package org.tcl.app.db.entities

import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.dao.IntEntity
import org.jetbrains.exposed.v1.dao.IntEntityClass
import org.tcl.app.db.tables.CourtsTable

class CourtEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<CourtEntity>(CourtsTable)

    var name by CourtsTable.name
}