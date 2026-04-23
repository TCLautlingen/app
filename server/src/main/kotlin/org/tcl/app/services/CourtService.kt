package org.tcl.app.services

import org.tcl.app.court.Court
import org.tcl.app.repositories.CourtRepository

class CourtService(
    private val courtRepository: CourtRepository
) {
    suspend fun getAllCourts(): List<Court> {
        return courtRepository.allCourts()
    }

    suspend fun getCourtById(id: Int): Court? {
        return courtRepository.courtById(id)
    }
}
