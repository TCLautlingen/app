package org.tcl.app.court.domain

import org.tcl.app.Court
import org.tcl.app.court.data.CourtApiService

class CourtRepository(
    private val api: CourtApiService
) {
    suspend fun getCourts(): List<Court> = api.getCourts()

    suspend fun getCourtById(courtId: Int): Court = api.getCourtById(courtId)
}