package com.akushch.adlershof.persistence.config

import io.r2dbc.postgresql.PostgresqlConnectionConfiguration
import io.r2dbc.postgresql.PostgresqlConnectionFactory
import io.r2dbc.spi.ConnectionFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.core.convert.converter.Converter
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration
import org.springframework.data.r2dbc.convert.R2dbcCustomConversions
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories
import org.testcontainers.containers.PostgreSQLContainer

@Configuration
@EnableR2dbcRepositories("com.akushch.adlershof.persistence.repository")
@ComponentScan("com.akushch.adlershof.persistence.facade")
@Import(CustomConvertersConfiguration::class)
class DatabaseTestConfiguration(
    val psqlContainer: PersistenceTestConfig.PSQLContainer,
    val converters: List<Converter<*, *>?>
) : AbstractR2dbcConfiguration() {
    override fun connectionFactory(): ConnectionFactory {
        return with(psqlContainer) {
            PostgresqlConnectionFactory(
                PostgresqlConnectionConfiguration.builder()
                    .host("localhost")
                    .port(getMappedPort(PostgreSQLContainer.POSTGRESQL_PORT))
                    .database(databaseName)
                    .username(username)
                    .password(password)
                    .build()
            )
        }
    }

    @Bean
    override fun r2dbcCustomConversions(): R2dbcCustomConversions = R2dbcCustomConversions(converters)
}