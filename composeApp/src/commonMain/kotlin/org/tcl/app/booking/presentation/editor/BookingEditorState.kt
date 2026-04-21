package org.tcl.app.booking.presentation.editor

import androidx.compose.runtime.Stable
import com.kizitonwose.calendar.core.now
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import org.tcl.app.booking.AvailableSlot

@Stable
data class BookingEditorState (
    val date: LocalDate = LocalDate.now(),
    val duration: Int = 60,
    val startTime: LocalTime? = null,
    val courtId: Int? = null,
    val availableSlots: List<AvailableSlot> = emptyList(),
    val isSaving: Boolean = false,
    val showDeleteDialog: Boolean = false,
    val showDateSheet: Boolean = false,
)