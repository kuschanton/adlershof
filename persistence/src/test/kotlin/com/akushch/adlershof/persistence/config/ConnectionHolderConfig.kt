package com.akushch.adlershof.persistence.config

import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.sql.DataSource

@Configuration
class ConnectionHolderConfig {
    @Bean
    fun connectionHolder(dataSource: DataSource): DatasourceConnectionHolder = DatasourceConnectionHolder(dataSource)
}