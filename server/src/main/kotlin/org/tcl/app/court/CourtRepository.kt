package org.tcl.app.court

interface CourtRepository {
    suspend fun allCourts(): List<Court>
    suspend fun courtById(id: Int): Court?
}