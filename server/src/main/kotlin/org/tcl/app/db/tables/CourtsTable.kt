package org.tcl.app.db.tables

import org.jetbrains.exposed.v1.core.dao.id.IntIdTable

object CourtsTable : IntIdTable("courts") {
    val name = varchar("name", 256)
}