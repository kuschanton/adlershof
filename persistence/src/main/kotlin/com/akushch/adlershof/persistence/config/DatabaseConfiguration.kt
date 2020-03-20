package com.akushch.adlershof.persistence.config

import io.r2dbc.postgresql.PostgresqlConnectionConfiguration
import io.r2dbc.postgresql.PostgresqlConnectionFactory
import io.r2dbc.spi.ConnectionFactory
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.core.convert.converter.Converter
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration
import org.springframework.data.r2dbc.convert.R2dbcCustomConversions
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories

@Configuration
@EnableR2dbcRepositories("com.akushch.adlershof.persistence.repository")
@ComponentScan("com.akushch.adlershof.persistence.facade")
@Import(CustomConvertersConfiguration::class)
@EnableConfigurationProperties(DatabaseProperties::class)
class DatabaseConfiguration(
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

    @Bean
    override fun r2dbcCustomConversions(): R2dbcCustomConversions = R2dbcCustomConversions(converters)
}