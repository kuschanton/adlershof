package com.akushch.adlershof.persistence.config

import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.core.convert.converter.Converter
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories

@Configuration
@EnableR2dbcRepositories("com.akushch.adlershof.persistence.repository")
@ComponentScan("com.akushch.adlershof.persistence.facade")
@Import(CustomConvertersConfiguration::class)
@EnableConfigurationProperties(DatabaseProperties::class)
class DatabaseConfiguration(
    databaseProperties: DatabaseProperties,
    converters: List<Converter<*, *>?>
) : R2dbcConfiguration(databaseProperties, converters)

