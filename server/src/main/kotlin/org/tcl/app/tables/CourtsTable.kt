package org.tcl.app.tables

import org.jetbrains.exposed.v1.core.dao.id.IntIdTable

object CourtsTable : IntIdTable() {
    val name = varchar("name", 256)
}