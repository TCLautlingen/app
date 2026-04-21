package org.tcl.app.court

import org.tcl.app.plugins.withTransaction

class PostgresCourtRepository() : CourtRepository {
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