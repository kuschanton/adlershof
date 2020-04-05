package com.akushch.adlershof.persistence

import com.akushch.adlershof.persistence.config.ConnectionHolderConfig
import com.akushch.adlershof.persistence.config.PostgresqlDataTypeFactory
import com.github.database.rider.core.api.configuration.DBUnit
import com.github.database.rider.junit5.api.DBRider
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.testcontainers.junit.jupiter.Testcontainers


@ExtendWith(SpringExtension::class)
@Import(ConnectionHolderConfig::class)
@SpringBootTest
@DBRider
@DBUnit(
    caseSensitiveTableNames = true,
    dataTypeFactoryClass = PostgresqlDataTypeFactory::class
)
@Testcontainers
annotation class PersistenceTest