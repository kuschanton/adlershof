package com.akushch.adlershof.persistence.config

import com.akushch.adlershof.persistence.config.PersistenceTestConfig.PSQLContainer
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.core.convert.converter.Converter
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories
import org.testcontainers.containers.PostgreSQLContainer

@Configuration
@EnableR2dbcRepositories("com.akushch.adlershof.persistence.repository")
@ComponentScan("com.akushch.adlershof.persistence.facade")
@Import(CustomConvertersConfiguration::class)
class DatabaseTestConfiguration(
    psqlContainer: PSQLContainer,
    converters: List<Converter<*, *>?>
) : R2dbcConfiguration(psqlContainer.toDatabaseProperties(), converters)

private fun PSQLContainer.toDatabaseProperties(): DatabaseProperties = DatabaseProperties.build(
    host = "localhost",
    port = getMappedPort(PostgreSQLContainer.POSTGRESQL_PORT),
    database = databaseName,
    username = username,
    password = password
)