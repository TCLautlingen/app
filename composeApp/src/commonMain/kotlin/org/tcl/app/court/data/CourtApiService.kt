package org.tcl.app.court.data

import io.ktor.client.call.body
import io.ktor.client.request.get
import org.tcl.app.Court
import org.tcl.app.core.data.ApiClient

class CourtApiService(
    private val apiClient: ApiClient
) {
    suspend fun getCourts(): List<Court> {
        return apiClient.client.get("/courts").body()
    }

    suspend fun getCourtById(id: Int): Court {
        return apiClient.client.get("/courts/$id").body()
    }
}