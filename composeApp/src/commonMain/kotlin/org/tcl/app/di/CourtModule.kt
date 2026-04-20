package org.tcl.app.di

import org.koin.dsl.module
import org.tcl.app.court.data.CourtApiService
import org.tcl.app.court.data.CourtRepositoryImpl
import org.tcl.app.court.domain.CourtRepository

val courtModule = module {
    single { CourtApiService(get()) }
    single<CourtRepository> { CourtRepositoryImpl(get()) }
}