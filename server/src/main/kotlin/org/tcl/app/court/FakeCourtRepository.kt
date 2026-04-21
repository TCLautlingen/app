package org.tcl.app.court

class FakeCourtRepository() : CourtRepository {
    private val courts = mutableListOf(
        Court(id = 1, name = "Court 1"),
        Court(id = 2, name = "Court 2"),
        Court(id = 3, name = "Court 3"),
        Court(id = 4, name = "Court 4"),
        Court(id = 5, name = "Court 5")
    )

    private var nextId = 1

    override suspend fun allCourts(): List<Court> {
        return courts
    }

    override suspend fun courtById(id: Int): Court? {
        return courts.firstOrNull { it.id == id }
    }


}