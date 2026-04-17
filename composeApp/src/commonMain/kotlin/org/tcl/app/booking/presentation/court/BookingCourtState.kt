package org.tcl.app.booking.presentation.court

import androidx.compose.runtime.Stable
import com.kizitonwose.calendar.core.now
import kotlinx.datetime.LocalDate
import org.tcl.app.Court
import org.tcl.app.CourtSlot

@Stable
data class BookingCourtState(
    val date: LocalDate = LocalDate.now(),
    val courtId: Int = 1,
    val courtSlots: List<CourtSlot> = emptyList(),
    val courts: List<Court> = emptyList(),
    val showDateSheet: Boolean = false,
)