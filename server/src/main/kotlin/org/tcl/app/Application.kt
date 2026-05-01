package org.tcl.app

import io.ktor.server.application.*
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import org.koin.ktor.plugin.Koin
import org.tcl.app.db.configureDatabases
import org.tcl.app.routes.configureRouting

fun main() {
    embeddedServer(
        Netty,
        port = System.getenv("PORT")?.toIntOrNull() ?: 8080,
        host = "0.0.0.0",
        module = Application::module
    ).start(wait = true)
}

fun Application.module() {
    install(Koin) {
        modules(appModule)
    }
    configureSerialization()
    configureHTTP()
    configureWebSockets()
    configureDatabases()
    configureRouting()
}