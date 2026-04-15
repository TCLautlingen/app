package org.tcl.app.booking.presentation.court

import com.kizitonwose.calendar.core.now
import kotlinx.datetime.LocalDate
import org.tcl.app.CourtSlot

data class BookingCourtState(
    val date: String = LocalDate.now().toString(),
    val court: Int = 1,
    val courtSlots: List<CourtSlot> = emptyList(),
)