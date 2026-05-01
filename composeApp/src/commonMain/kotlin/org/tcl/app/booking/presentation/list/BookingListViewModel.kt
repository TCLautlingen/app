package org.tcl.app.booking.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kizitonwose.calendar.core.now
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import org.tcl.app.booking.BookingWsMessage
import org.tcl.app.booking.data.BookingWebSocketDataSource
import org.tcl.app.booking.domain.BookingRemoteDataSource
import org.tcl.app.core.domain.util.onFailure
import org.tcl.app.core.domain.util.onSuccess
import org.tcl.app.user.domain.UserRemoteDataSource

class BookingListViewModel(
    private val dataSource: BookingRemoteDataSource,
    private val wsDataSource: BookingWebSocketDataSource,
    private val userRemoteDataSource: UserRemoteDataSource,
) : ViewModel() {
    private val _state = MutableStateFlow(BookingListState())
    val state = _state.asStateFlow()

    init {
        updateBookings()
        observeWebSocketMessages()
        observeConnectionState()
    }

    private fun observeWebSocketMessages() {
        viewModelScope.launch {
            wsDataSource.messages.collect { message ->
                when (message) {
                    is BookingWsMessage.BookingCreated -> _state.update { current ->
                        val alreadyPresent = current.bookings.any { b -> b.id == message.booking.id }
                        if (alreadyPresent) current
                        else {
                            val updated = (current.bookings + message.booking)
                                .sortedWith(compareBy({ it.date }, { it.startTime }))
                            current.copy(bookings = updated)
                        }
                    }
                    is BookingWsMessage.BookingDeleted -> _state.update { current ->
                        current.copy(bookings = current.bookings.filter { b -> b.id != message.bookingId })
                    }
                }
            }
        }
    }

    private fun observeConnectionState() {
        viewModelScope.launch {
            var previouslyConnected = false
            wsDataSource.isConnected.collect { isConnected ->
                _state.update { it.copy(isOffline = !isConnected) }
                // Re-sync on reconnect to recover any messages missed during disconnection
                if (!previouslyConnected && isConnected) {
                    updateBookings()
                }
                previouslyConnected = isConnected
            }
        }
    }

    fun onAction(action: BookingListAction) {
        when (action) {
            is BookingListAction.OnDeleteClick ->
                _state.update { it.copy(showDeleteDialog = true, bookingIdToDelete = action.bookingId) }
            is BookingListAction.OnConfirmDelete -> viewModelScope.launch {
                _state.update { it.copy(isLoading = true) }
                if (_state.value.bookingIdToDelete != null) {
                    dataSource.deleteBooking(_state.value.bookingIdToDelete!!)
                        .onSuccess {
                            _state.update {
                                it.copy(isLoading = false, showDeleteDialog = false, bookingIdToDelete = null)
                            }
                        }
                        .onFailure {
                            _state.update { it.copy(isLoading = false) }
                        }
                }
            }
            is BookingListAction.OnDismissDeleteDialog ->
                _state.update { it.copy(showDeleteDialog = false) }
            is BookingListAction.OnRefresh -> updateBookings()
        }
    }

    private fun updateBookings() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            dataSource.getUpcomingBookings(LocalDate.now().toString())
                .onSuccess { bookings ->
                    _state.update { it.copy(bookings = bookings, isLoading = false) }
                }
                .onFailure {
                    _state.update { it.copy(bookings = emptyList(), isLoading = false) }
                }
        }
    }
}
