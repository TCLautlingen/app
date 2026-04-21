package org.tcl.app.booking.presentation.court

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.tcl.app.booking.domain.BookingRemoteDataSource
import org.tcl.app.core.domain.util.onFailure
import org.tcl.app.core.domain.util.onSuccess
import org.tcl.app.court.domain.CourtRemoteDataSource

class BookingCourtViewModel(
    private val dataSource: BookingRemoteDataSource,
    private val courtRemoteDataSource: CourtRemoteDataSource
) : ViewModel() {
    private val _state = MutableStateFlow(BookingCourtState())
    val state = _state.asStateFlow()

    init {
        loadCourts()
        loadCourtSlots()
    }

    fun onAction(action: BookingCourtAction) {
        when (action) {
            is BookingCourtAction.OnDateClick -> _state.update { it.copy(showDateSheet = true) }
            is BookingCourtAction.OnDateChange -> {
                _state.update { it.copy(date = action.date, showDateSheet = false) }
                loadCourtSlots()
            }
            is BookingCourtAction.OnDateChangeDismiss -> _state.update { it.copy(showDateSheet = false) }
            is BookingCourtAction.OnRefresh -> {
                loadCourts()
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
            dataSource.getCourtSlots(currentState.courtId, currentState.date.toString())
                .onSuccess { courtSlots ->
                    _state.update {
                        it.copy(
                            courtSlots = courtSlots
                        )
                    }
                }
                .onFailure {

                }
        }
    }

    private fun loadCourts() {
        viewModelScope.launch {
            courtRemoteDataSource.getCourts()
                .onSuccess { courts ->
                    _state.update {
                        it.copy(
                            courts = courts
                        )
                    }
                }
        }
    }
}