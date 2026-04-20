package org.tcl.app.core.domain.util

import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import kotlinx.io.IOException

sealed interface DataError : Error {
    object Network : DataError
    object Unauthorized : DataError
    object NotFound : DataError
    object Unknown : DataError
}

suspend inline fun <reified T> safeApiCall(
    execute: () -> HttpResponse
): Result<T, DataError> {
    return try {
        val response = execute()
        when (response.status.value) {
            in 200..299 -> Result.Success(response.body())
            401 -> Result.Error(DataError.Unauthorized)
            404 -> Result.Error(DataError.NotFound)
            in 500..599 -> Result.Error(DataError.Network)
            else -> Result.Error(DataError.Unknown)
        }
    } catch (e: IOException) {
        Result.Error(DataError.Network) // true network failure, no response at all
    } catch (e: Exception) {
        Result.Error(DataError.Unknown)
    }
}