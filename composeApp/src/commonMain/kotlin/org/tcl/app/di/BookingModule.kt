package org.tcl.app.di

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import org.tcl.app.booking.presentation.editor.BookingEditorViewModel
import org.tcl.app.booking.presentation.list.BookingListViewModel

val bookingModule = module {
    viewModelOf(::BookingListViewModel)
    viewModelOf(::BookingEditorViewModel)
}