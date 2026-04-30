package org.tcl.app.db

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.application.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.JdbcTransaction
import org.jetbrains.exposed.v1.jdbc.SchemaUtils
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.inTopLevelSuspendTransaction
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.tcl.app.db.tables.BookingPlayersTable
import org.tcl.app.db.tables.BookingsTable
import org.tcl.app.db.tables.CourtsTable
import org.tcl.app.db.tables.NotificationTokensTable
import org.tcl.app.db.tables.NotificationsTable
import org.tcl.app.db.tables.ProfilesTable
import org.tcl.app.db.tables.RefreshTokensTable
import org.tcl.app.db.tables.UsersTable

fun Application.configureDatabases() {
    val dbUrl = System.getenv("DATABASE_URL")
    val dbUser = System.getenv("DATABASE_USER")
    val dbPassword = System.getenv("DATABASE_PASSWORD")

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

    transaction {
        SchemaUtils.create(
            UsersTable,
            ProfilesTable,
            CourtsTable,
            RefreshTokensTable,
            BookingsTable,
            BookingPlayersTable,
            NotificationsTable,
            NotificationTokensTable
        )

        if (CourtsTable.selectAll().empty()) {
            CourtsTable.insert { it[name] = "Platz 1" }
            CourtsTable.insert { it[name] = "Platz 2" }
            CourtsTable.insert { it[name] = "Platz 3" }
            CourtsTable.insert { it[name] = "Platz 4" }
            CourtsTable.insert { it[name] = "Platz 5" }
        }
    }
}

suspend fun <T> withTransaction(block: suspend JdbcTransaction.() -> T): T = withContext(Dispatchers.IO) {
    inTopLevelSuspendTransaction {
        block()
    }
}