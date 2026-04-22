package org.tcl.app

import io.ktor.server.application.*
import org.koin.ktor.plugin.Koin
import org.tcl.app.di.appModule
import org.tcl.app.plugins.configureAuthentication
import org.tcl.app.plugins.configureDatabases
import org.tcl.app.plugins.configureRouting
import org.tcl.app.plugins.configureSerialization
import org.tcl.app.plugins.configureStatusPages

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.module() {
    install(Koin) {
        modules(appModule)
    }
    configureDatabases()
    configureSerialization()
    configureStatusPages()
    configureAuthentication()
    configureRouting()
}