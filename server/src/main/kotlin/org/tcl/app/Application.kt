package org.tcl.app

import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import org.koin.ktor.plugin.Koin
import org.tcl.app.di.appModule
import org.tcl.app.plugins.configureAuthentication
import org.tcl.app.plugins.configureDatabases
import org.tcl.app.plugins.configureRouting
import org.tcl.app.plugins.configureSerialization

fun main() {
    embeddedServer(Netty, port = SERVER_PORT, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    install(Koin) {
        modules(appModule)
    }
    configureDatabases()
    configureSerialization()
    configureAuthentication()
    configureRouting()
}