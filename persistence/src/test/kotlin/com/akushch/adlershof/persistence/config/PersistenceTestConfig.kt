package com.akushch.adlershof.persistence.config

import com.zaxxer.hikari.HikariDataSource
import org.slf4j.LoggerFactory.getLogger
import org.springframework.boot.SpringBootConfiguration
import org.springframework.boot.test.autoconfigure.data.jdbc.AutoConfigureDataJdbc
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.containers.output.Slf4jLogConsumer
import javax.sql.DataSource

@SpringBootConfiguration
@AutoConfigureDataJdbc
@Import(DatabaseTestConfiguration::class)
class PersistenceTestConfig {

    @Bean(initMethod = "start", destroyMethod = "stop")
    fun postgres(): PSQLContainer = PSQLContainer().withLogConsumer(Slf4jLogConsumer(getLogger("POSTGRESQL")))

    @Bean
    fun dataSource(psqlContainer: PSQLContainer): DataSource = HikariDataSource().apply {
        driverClassName = "org.postgresql.Driver"
        jdbcUrl = psqlContainer.jdbcUrl
        username = psqlContainer.username
        password = psqlContainer.password
    }

    @Bean
    fun connectionHolder(dataSource: DataSource): DatasourceConnectionHolder = DatasourceConnectionHolder(dataSource)

    class PSQLContainer : PostgreSQLContainer<PSQLContainer>("postgres:10.5")
}