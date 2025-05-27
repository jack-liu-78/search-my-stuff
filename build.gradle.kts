
plugins {
    kotlin("jvm") version "1.9.10"
    id("io.ktor.plugin") version "2.3.5"
    kotlin("plugin.serialization") version "1.9.10"
}

application {
    mainClass.set("com.example.searchstuff.ApplicationKt")
}

repositories {
    mavenCentral()
}

dependencies {
    // Ktor core
    implementation("io.ktor:ktor-server-core-jvm")
    implementation("io.ktor:ktor-server-netty-jvm")
    implementation("io.ktor:ktor-server-content-negotiation")
    implementation("io.ktor:ktor-serialization-kotlinx-json")
    implementation("io.ktor:ktor-server-call-logging")

    // Logging
    implementation("ch.qos.logback:logback-classic:1.4.11")

    implementation("co.elastic.clients:elasticsearch-java:8.11.1")

    implementation("com.fasterxml.jackson.core:jackson-databind:2.15.2")

    // PostgreSQL JDBC (works with CockroachDB)
    implementation("org.postgresql:postgresql:42.6.0")

    // Exposed ORM
    implementation("org.jetbrains.exposed:exposed-core:0.43.0")
    implementation("org.jetbrains.exposed:exposed-dao:0.43.0")
    implementation("org.jetbrains.exposed:exposed-jdbc:0.43.0")

    // Kotlinx datetime
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.4.1")

    // Test
    testImplementation("io.ktor:ktor-server-tests-jvm")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
}

ktor {
    docker {
        localImageName.set("search-my-stuff")
        imageTag.set("0.1.0")
    }
    fatJar {
        archiveFileName.set("search-my-stuff.jar")
    }
}