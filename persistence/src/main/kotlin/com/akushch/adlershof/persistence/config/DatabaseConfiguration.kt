package com.akushch.adlershof.persistence.config

import io.r2dbc.postgresql.PostgresqlConnectionConfiguration
import io.r2dbc.postgresql.PostgresqlConnectionFactory
import io.r2dbc.spi.ConnectionFactory
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories

@Configuration
@EnableR2dbcRepositories("com.akushch.adlershof.persistence.repository")
@ComponentScan("com.akushch.adlershof.persistence.facade")
@EnableConfigurationProperties(DatabaseProperties::class)
class DatabaseConfiguration(
    val databaseProperties: DatabaseProperties
) : AbstractR2dbcConfiguration() {
    override fun connectionFactory(): ConnectionFactory {
        return with(databaseProperties) {
            PostgresqlConnectionFactory(
                PostgresqlConnectionConfiguration.builder()
                    .host(host)
                    .port(port)
                    .database(database)
                    .username(username)
                    .password(password).build()
            )
        }
    }
}