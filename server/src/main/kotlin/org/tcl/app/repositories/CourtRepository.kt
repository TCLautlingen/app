package org.tcl.app.repositories

import org.tcl.app.court.Court

interface CourtRepository {
    suspend fun allCourts(): List<Court>
    suspend fun courtById(id: Int): Court?
}
