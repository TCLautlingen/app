package org.tcl.app.di

import org.koin.dsl.module
import org.tcl.app.court.data.FakeCourtRemoteDataSource
import org.tcl.app.court.data.KtorCourtRemoteDataSource
import org.tcl.app.court.domain.CourtRemoteDataSource

val courtModule = module {
    single<CourtRemoteDataSource> {
        if (TESTING) FakeCourtRemoteDataSource()
        else KtorCourtRemoteDataSource(get())
    }
}