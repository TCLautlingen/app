package org.tcl.app.di

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import org.tcl.app.booking.data.BookingApiService
import org.tcl.app.booking.domain.BookingRepository
import org.tcl.app.booking.presentation.court.BookingCourtViewModel
import org.tcl.app.booking.presentation.editor.BookingEditorViewModel
import org.tcl.app.booking.presentation.list.BookingListViewModel

val bookingModule = module {
    single { BookingApiService(get()) }
    single { BookingRepository(get()) }


    viewModelOf(::BookingListViewModel)
    viewModelOf(::BookingEditorViewModel)
    viewModelOf(::BookingCourtViewModel)
}