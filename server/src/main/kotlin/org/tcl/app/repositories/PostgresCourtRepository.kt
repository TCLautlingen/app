package org.tcl.app.repositories

import org.tcl.app.court.Court
import org.tcl.app.entities.CourtEntity
import org.tcl.app.mappers.entityToCourt
import org.tcl.app.plugins.withTransaction

class PostgresCourtRepository : CourtRepository {
    override suspend fun allCourts(): List<Court> = withTransaction {
        CourtEntity
            .all()
            .map(::entityToCourt)
    }

    override suspend fun courtById(id: Int): Court? = withTransaction {
        CourtEntity
            .findById(id)
            ?.let(::entityToCourt)
    }
}
