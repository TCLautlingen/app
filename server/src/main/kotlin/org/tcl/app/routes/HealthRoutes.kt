package org.tcl.app.routes

import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.v1.jdbc.transactions.suspendTransaction
import org.tcl.app.BuildInfo
import org.tcl.app.models.HealthResponse

fun Route.healthRoutes() {
    get("/health") {
        val postgresStatus = try {
            suspendTransaction { exec("SELECT 1") }
            "ok"
        } catch (e: Exception) {
            "error: ${e.message}"
        }

        val allHealthy = postgresStatus == "ok"
        val status = if (postgresStatus == "ok") {
            if (allHealthy) "healthy" else "degraded"
        } else "unhealthy"
        val httpStatus = if (postgresStatus == "ok") HttpStatusCode.OK else HttpStatusCode.ServiceUnavailable

        call.respond(
            httpStatus,
            HealthResponse(
                status = status,
                postgres = postgresStatus,
                version = BuildInfo.version,
            )
        )
    }
}