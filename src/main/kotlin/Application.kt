package com.example

import Items
import io.ktor.server.application.*
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.Database

fun main(args: Array<String>) {
    embeddedServer(Netty, port = 8080) {
        module()
    }.start(wait = true)
}


fun connectToDb() {
    Database.connect(
        url = "jdbc:postgresql://localhost:26257/defaultdb",
        driver = "org.postgresql.Driver",
        user = "root",
        password = ""
    )

    transaction {
        SchemaUtils.create(Items)
    }
}

fun Application.module() {
    connectToDb()
    configureSerialization()
    configureRouting()
}
