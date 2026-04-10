package org.tcl.app.booking.presentation.editor

import com.kizitonwose.calendar.core.now
import kotlinx.datetime.LocalDate

data class BookingEditorState (
    val date: LocalDate = LocalDate.now(),
    val duration: Int = 30,
    val startTime: String = "10:00",
    val court: Int = 1,
    val isSaving: Boolean = false,
    val showDeleteDialog: Boolean = false,
)