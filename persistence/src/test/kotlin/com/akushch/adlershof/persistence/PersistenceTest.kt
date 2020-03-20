package com.akushch.adlershof.persistence

import com.akushch.adlershof.persistence.config.PersistenceTestConfig
import com.akushch.adlershof.persistence.config.PostgresqlDataTypeFactory
import com.github.database.rider.core.api.configuration.DBUnit
import com.github.database.rider.junit5.api.DBRider
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.NONE
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.testcontainers.junit.jupiter.Testcontainers


@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = NONE, classes = [PersistenceTestConfig::class])
@ActiveProfiles(profiles = ["test"])
@DBRider
@DBUnit(
    caseSensitiveTableNames = true,
    dataTypeFactoryClass = PostgresqlDataTypeFactory::class
)
@Testcontainers
annotation class PersistenceTest