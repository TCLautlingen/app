package org.tcl.app.booking.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.tcl.app.booking.domain.BookingRepository
import org.tcl.app.core.domain.util.onFailure
import org.tcl.app.core.domain.util.onSuccess

class BookingListViewModel(
    private val repository: BookingRepository
) : ViewModel() {
    private val _state = MutableStateFlow(BookingListState())
    val state = _state.asStateFlow()

    init {
        updateBookings()
    }

    fun onAction(action: BookingListAction) {
        when (action) {
            is BookingListAction.OnDeleteClick -> {
                _state.update { it.copy(showDeleteDialog = true, bookingIdToDelete = action.bookingId) }
            }
            is BookingListAction.OnConfirmDelete -> {
                viewModelScope.launch {
                    _state.update { it.copy(isLoading = true) }
                    try {
                        repository.deleteBooking(_state.value.bookingIdToDelete ?: "")
                        updateBookings()
                        _state.update { it.copy(showDeleteDialog = false, bookingIdToDelete = null) }
                    } catch (e: Exception) {
                        _state.update { it.copy(isLoading = false) }
                    }
                }
            }
            is BookingListAction.OnDismissDeleteDialog -> {
                _state.update { it.copy(showDeleteDialog = false) }
            }
            is BookingListAction.OnRefresh -> updateBookings()
        }
    }

    private fun updateBookings() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            repository.getBookings()
                .onSuccess { bookings ->
                    _state.update {
                        it.copy(
                            bookings = bookings.map { b -> b.toBookingUi() },
                            isLoading = false
                        )
                    }
                }
                .onFailure {
                    _state.update { it.copy(
                        bookings = emptyList(),
                        isLoading = false
                    ) }
                }
        }
    }
}