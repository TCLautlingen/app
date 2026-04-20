package org.tcl.app.court.data

import org.tcl.app.Court
import org.tcl.app.core.domain.util.DataError
import org.tcl.app.core.domain.util.Result
import org.tcl.app.court.domain.CourtRepository

class CourtRepositoryImpl(
    private val api: CourtApiService
) : CourtRepository {
    override suspend fun getCourts(): Result<List<Court>, DataError> = api.getCourts()

    override suspend fun getCourtById(courtId: Int): Result<Court, DataError> = api.getCourtById(courtId)
}