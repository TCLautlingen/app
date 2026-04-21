package org.tcl.app.court.data

import io.ktor.client.call.body
import io.ktor.client.request.get
import org.tcl.app.Court
import org.tcl.app.core.data.ApiClient
import org.tcl.app.core.domain.util.DataError
import org.tcl.app.core.domain.util.Result
import org.tcl.app.core.domain.util.safeApiCall
import org.tcl.app.court.domain.CourtRemoteDataSource

class KtorCourtRemoteDataSource(
    private val apiClient: ApiClient
) : CourtRemoteDataSource {
    override suspend fun getCourts(): Result<List<Court>, DataError> = safeApiCall {
        apiClient.client.get("/courts").body()
    }

    override suspend fun getCourtById(id: Int): Result<Court, DataError> = safeApiCall {
        apiClient.client.get("/courts/$id").body()
    }
}