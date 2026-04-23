package org.tcl.app.repositories

import org.tcl.app.court.Court
import org.tcl.app.models.CourtDAO
import org.tcl.app.models.daoToCourt
import org.tcl.app.plugins.withTransaction

class PostgresCourtRepository : CourtRepository {
    override suspend fun allCourts(): List<Court> = withTransaction {
        CourtDAO
            .all()
            .map(::daoToCourt)
    }

    override suspend fun courtById(id: Int): Court? = withTransaction {
        CourtDAO
            .findById(id)
            ?.let(::daoToCourt)
    }
}
