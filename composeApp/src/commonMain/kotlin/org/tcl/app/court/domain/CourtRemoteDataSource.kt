package org.tcl.app.court.domain

import org.tcl.app.Court
import org.tcl.app.core.domain.util.DataError
import org.tcl.app.core.domain.util.Result

interface CourtRemoteDataSource {
    suspend fun getCourts(): Result<List<Court>, DataError>
    suspend fun getCourtById(courtId: Int): Result<Court, DataError>
}