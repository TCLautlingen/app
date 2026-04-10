package org.tcl.app.booking.presentation.editor

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class BookingEditorViewModel(

) : ViewModel() {
    private val _state = MutableStateFlow(BookingEditorState())
    val state = _state.asStateFlow()


    fun onAction(action: BookingEditorAction) {
        when (action) {
            is BookingEditorAction.OnDateChange -> {
                _state.update { it.copy(date = action.date) }
            }
            is BookingEditorAction.OnDurationChange -> {
                _state.update { it.copy(duration = action.duration) }
            }
            is BookingEditorAction.OnStartTimeChange -> {
                _state.update { it.copy(startTime = action.startTime) }
            }
            is BookingEditorAction.OnCourtChange -> {
                _state.update { it.copy(court = action.court) }
            }
        }
    }
}