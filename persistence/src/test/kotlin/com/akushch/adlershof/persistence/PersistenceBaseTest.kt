package com.akushch.adlershof.persistence

import com.akushch.adlershof.persistence.container.PSQLContainer
import org.slf4j.LoggerFactory
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.containers.output.Slf4jLogConsumer
import org.testcontainers.junit.jupiter.Container

abstract class PersistenceBaseTest {
    companion object {
        @JvmStatic
        @Container
        val psqlContainer: PSQLContainer = PSQLContainer()
            .withLogConsumer(Slf4jLogConsumer(LoggerFactory.getLogger("POSTGRESQL")))

        @JvmStatic
        @DynamicPropertySource
        fun psqlProperties(registry: DynamicPropertyRegistry) {
            // R2DBC
            registry.add("spring.data.postgres.host") { "localhost" }
            registry.add("spring.data.postgres.port") { psqlContainer.getMappedPort(PostgreSQLContainer.POSTGRESQL_PORT) }
            registry.add("spring.data.postgres.database") { psqlContainer.databaseName }
            registry.add("spring.data.postgres.username") { psqlContainer.username }
            registry.add("spring.data.postgres.password") { psqlContainer.password }

            // JDBC
            registry.add("spring.datasource.type") { "com.zaxxer.hikari.HikariDataSource" }
            registry.add("spring.datasource.driver-class-name") { "org.postgresql.Driver" }
            registry.add("spring.datasource.url") {
                val port = psqlContainer.getMappedPort(PostgreSQLContainer.POSTGRESQL_PORT)
                val database = psqlContainer.databaseName
                "jdbc:postgresql://localhost:$port/$database"
            }
            registry.add("spring.datasource.username") { psqlContainer.username }
            registry.add("spring.datasource.password") { psqlContainer.password }
        }
    }
}