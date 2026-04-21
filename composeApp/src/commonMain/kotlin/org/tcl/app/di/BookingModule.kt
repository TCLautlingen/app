package org.tcl.app.di

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import org.tcl.app.booking.data.FakeBookingRemoteDataSource
import org.tcl.app.booking.data.KtorBookingRemoteDataSource
import org.tcl.app.booking.domain.BookingRemoteDataSource
import org.tcl.app.booking.presentation.court.BookingCourtViewModel
import org.tcl.app.booking.presentation.editor.BookingEditorViewModel
import org.tcl.app.booking.presentation.list.BookingListViewModel
import org.tcl.app.booking.presentation.success.BookingSuccessViewModel

val bookingModule = module {
    single<BookingRemoteDataSource> {
        if (TESTING) FakeBookingRemoteDataSource()
        else KtorBookingRemoteDataSource(get())
    }

    viewModelOf(::BookingListViewModel)
    viewModelOf(::BookingEditorViewModel)
    viewModelOf(::BookingSuccessViewModel)
    viewModelOf(::BookingCourtViewModel)
}