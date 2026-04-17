package org.tcl.app.court

import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.core.dao.id.IntIdTable
import org.jetbrains.exposed.v1.dao.IntEntity
import org.jetbrains.exposed.v1.dao.IntEntityClass
import org.tcl.app.Court

object CourtTable: IntIdTable("court") {
    val name = varchar("name", 255)
}

class CourtDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<CourtDAO>(CourtTable)

    var name by CourtTable.name
}

fun daoToCourt(dao: CourtDAO) = Court(
    id = dao.id.value,
    name = dao.name
)