package org.tcl.app.di

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import org.tcl.app.booking.data.BookingApiService
import org.tcl.app.booking.data.BookingRepositoryImpl
import org.tcl.app.booking.domain.BookingRepository
import org.tcl.app.booking.presentation.court.BookingCourtViewModel
import org.tcl.app.booking.presentation.editor.BookingEditorViewModel
import org.tcl.app.booking.presentation.list.BookingListViewModel
import org.tcl.app.booking.presentation.success.BookingSuccessViewModel

val bookingModule = module {
    single { BookingApiService(get()) }
    single<BookingRepository> { BookingRepositoryImpl(get()) }


    viewModelOf(::BookingListViewModel)
    viewModelOf(::BookingEditorViewModel)
    viewModelOf(::BookingSuccessViewModel)
    viewModelOf(::BookingCourtViewModel)
}