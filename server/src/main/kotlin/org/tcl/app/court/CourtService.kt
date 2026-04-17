package org.tcl.app.court

import org.tcl.app.Court

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