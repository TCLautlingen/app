package org.tcl.app.plugins

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.application.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.JdbcTransaction
import org.jetbrains.exposed.v1.jdbc.transactions.inTopLevelSuspendTransaction
import org.tcl.app.di.TESTING

fun Application.configureDatabases() {
    if (TESTING) {
        return
    }

    val dbUrl = environment.config.property("database.url").getString()
    val dbUser = environment.config.property("database.user").getString()
    val dbPassword = environment.config.property("database.password").getString()

    val hikariConfig = HikariConfig().apply {
        driverClassName = "org.postgresql.Driver"
        jdbcUrl = dbUrl
        username = dbUser
        password = dbPassword
        maximumPoolSize = 10
        isAutoCommit = false
        transactionIsolation = "TRANSACTION_READ_COMMITTED"
        validate()
    }

    Database.connect(
        HikariDataSource(hikariConfig)
    )
}

suspend fun <T> withTransaction(block: suspend JdbcTransaction.() -> T): T = withContext(Dispatchers.IO) {
    inTopLevelSuspendTransaction {
        block()
    }
}