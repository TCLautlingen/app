package org.tcl.app.court.data

import org.tcl.app.court.Court
import org.tcl.app.core.domain.util.DataError
import org.tcl.app.core.domain.util.Result
import org.tcl.app.court.domain.CourtRemoteDataSource

class FakeCourtRemoteDataSource : CourtRemoteDataSource {
    private val courts = mutableListOf(
        Court(id = 1, name = "Platz 1"),
        Court(id = 2, name = "Platz 2"),
        Court(id = 3, name = "Platz 3"),
        Court(id = 4, name = "Platz 4"),
        Court(id = 5, name = "Platz 5"),
    )

    override suspend fun getCourts(): Result<List<Court>, DataError> {
        return Result.Success(courts)
    }

    override suspend fun getCourtById(courtId: Int): Result<Court, DataError> {
        val court = courts.find { it.id == courtId }
        return if (court != null) {
            Result.Success(court)
        }
        else {
            Result.Error(DataError.NotFound)
        }
    }
}