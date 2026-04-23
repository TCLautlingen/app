package org.tcl.app.mappers

import org.tcl.app.court.Court
import org.tcl.app.entities.CourtEntity

fun entityToCourt(entity: CourtEntity) = Court(
    id = entity.id.value,
    name = entity.name
)