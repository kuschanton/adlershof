package com.akushch.adlershof.persistence.config

import io.r2dbc.postgresql.PostgresqlConnectionConfiguration
import io.r2dbc.postgresql.PostgresqlConnectionFactory
import io.r2dbc.spi.ConnectionFactory
import org.springframework.core.convert.converter.Converter
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration
import org.springframework.data.r2dbc.convert.R2dbcCustomConversions

abstract class R2dbcConfiguration(
    val databaseProperties: DatabaseProperties,
    val converters: List<Converter<*, *>?>
) : AbstractR2dbcConfiguration() {
    override fun connectionFactory(): ConnectionFactory {
        return with(databaseProperties) {
            PostgresqlConnectionFactory(
                PostgresqlConnectionConfiguration.builder()
                    .host(host)
                    .port(port)
                    .database(database)
                    .username(username)
                    .password(password)
                    .build()
            )
        }
    }

    override fun r2dbcCustomConversions(): R2dbcCustomConversions = R2dbcCustomConversions(converters)
}