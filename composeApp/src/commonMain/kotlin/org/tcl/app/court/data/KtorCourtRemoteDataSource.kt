package org.tcl.app.court.data

import io.ktor.client.call.body
import io.ktor.client.request.get
import org.tcl.app.court.Court
import org.tcl.app.core.data.network.BackendApiClient
import org.tcl.app.core.domain.util.DataError
import org.tcl.app.core.domain.util.Result
import org.tcl.app.core.domain.util.safeApiCall
import org.tcl.app.court.domain.CourtRemoteDataSource

class KtorCourtRemoteDataSource(
    private val backendApiClient: BackendApiClient
) : CourtRemoteDataSource {
    override suspend fun getCourts(): Result<List<Court>, DataError> = safeApiCall {
        backendApiClient.client.get("courts").body()
    }

    override suspend fun getCourtById(id: Int): Result<Court, DataError> = safeApiCall {
        backendApiClient.client.get("courts/$id").body()
    }
}