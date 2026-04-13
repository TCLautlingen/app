package org.tcl.app.booking.presentation.editor

import com.kizitonwose.calendar.core.now
import kotlinx.datetime.LocalDate
import org.tcl.app.AvailableSlot

data class BookingEditorState (
    val date: LocalDate = LocalDate.now(),
    val duration: Int = 60,
    val startTime: String? = null,
    val court: Int? = null,
    val availableSlots: List<AvailableSlot> = emptyList(),
    val isSaving: Boolean = false,
    val showDeleteDialog: Boolean = false,
)