package org.tcl.app.repositories

import org.tcl.app.court.Court

class FakeCourtRepository : CourtRepository {
    private val courts = mutableListOf(
        Court(id = 1, name = "Platz 1"),
        Court(id = 2, name = "Platz 2"),
        Court(id = 3, name = "Platz 3"),
        Court(id = 4, name = "Platz 4"),
        Court(id = 5, name = "Platz 5")
    )

    override suspend fun allCourts(): List<Court> {
        return courts
    }

    override suspend fun courtById(id: Int): Court? {
        return courts.firstOrNull { it.id == id }
    }
}
