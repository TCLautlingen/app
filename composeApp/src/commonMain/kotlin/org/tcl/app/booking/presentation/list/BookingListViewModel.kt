package org.tcl.app.booking.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kizitonwose.calendar.core.now
import com.kizitonwose.calendar.core.plusDays
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.datetime.LocalDate
import org.tcl.app.booking.domain.Booking
import kotlin.collections.copy
import kotlin.collections.emptyList

class BookingListViewModel(

) : ViewModel() {
    private val _state = MutableStateFlow(BookingListState())
    val state = _state.asStateFlow()

    init {
        observeBookings()
    }

    fun onAction(action: BookingListAction) {
        when (action) {
            is BookingListAction.OnDeleteClick -> {
                _state.update { it.copy(showDeleteDialog = true) }
            }
            is BookingListAction.OnConfirmDelete -> {

            }
            is BookingListAction.OnDismissDeleteDialog -> {
                _state.update { it.copy(showDeleteDialog = false) }
            }
        }
    }

    private fun observeBookings() {
        flowOf(
            listOf(
                Booking(
                    id = "1",
                    date = LocalDate.now(),
                    startTime = "10:00",
                    duration = 30,
                    court = 1
                ),
                Booking(
                    id = "2",
                    date = LocalDate.now(),
                    startTime = "11:00",
                    duration = 60,
                    court = 2
                ),
                Booking(
                    id = "3",
                    date = LocalDate.now().plusDays(1),
                    startTime = "12:00",
                    duration = 90,
                    court = 3
                ),
            )
            /*
            emptyList<Booking>()
            */
        )
        .onEach { bookings ->
            _state.update {
                it.copy(
                    bookings = bookings.map { booking -> booking.toBookingUi() },
                    isLoading = false,
                )
            }
        }
        .launchIn(viewModelScope)
    }
}