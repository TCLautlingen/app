package org.tcl.app.repositories.postgres

import org.tcl.app.court.Court
import org.tcl.app.db.entities.CourtEntity
import org.tcl.app.mappers.entityToCourt
import org.tcl.app.db.withTransaction
import org.tcl.app.repositories.CourtRepository

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
