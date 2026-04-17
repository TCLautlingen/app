package org.tcl.app.court

import org.tcl.app.Court

interface CourtRepository {
    suspend fun allCourts(): List<Court>
    suspend fun courtById(id: Int): Court?
}