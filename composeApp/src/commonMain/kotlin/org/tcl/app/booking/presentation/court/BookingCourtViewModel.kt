package org.tcl.app.booking.presentation.court

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.tcl.app.booking.domain.BookingRepository

class BookingCourtViewModel(
    private val repository: BookingRepository
) : ViewModel() {
    private val _state = MutableStateFlow(BookingCourtState())
    val state = _state.asStateFlow()

    init {
        loadCourtSlots()
    }

    fun onAction(action: BookingCourtAction) {
        when (action) {
            is BookingCourtAction.OnRefresh -> {
                loadCourtSlots()
            }
            is BookingCourtAction.OnCourtChange -> {
                _state.update {
                    it.copy(
                        courtId = action.court
                    )
                }
                loadCourtSlots()
            }
        }
    }

    private fun loadCourtSlots() {
        viewModelScope.launch {
            val currentState = _state.value
            val courtSlots = repository.getCourtSlots(currentState.courtId, currentState.date.toString())
            _state.update {
                it.copy(
                    courtSlots = courtSlots
                )
            }
        }
    }
}