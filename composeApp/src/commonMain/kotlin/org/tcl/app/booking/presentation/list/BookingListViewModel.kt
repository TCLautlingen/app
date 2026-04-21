package org.tcl.app.booking.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kizitonwose.calendar.core.now
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import org.tcl.app.booking.domain.BookingRemoteDataSource
import org.tcl.app.core.domain.util.onFailure
import org.tcl.app.core.domain.util.onSuccess

class BookingListViewModel(
    private val dataSource: BookingRemoteDataSource
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
                        dataSource.deleteBooking(_state.value.bookingIdToDelete ?: "")
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
            dataSource.getUpcomingBookings(LocalDate.now().toString())
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