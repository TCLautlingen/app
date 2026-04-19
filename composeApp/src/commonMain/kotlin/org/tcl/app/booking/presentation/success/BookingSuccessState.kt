package org.tcl.app.booking.presentation.success

import androidx.compose.runtime.Stable
import com.kizitonwose.calendar.core.now
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime

@Stable
data class BookingSuccessState(
    val date: LocalDate = LocalDate.now(),
    val startTime: LocalTime = LocalTime(12, 0),
    val durationMinutes: Int = 60,
    val courtName: String = "",
)