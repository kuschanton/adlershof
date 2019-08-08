package com.akushch.adlershof.feeder.config

import com.akushch.adlershof.persistence.config.DatabaseConfiguration
import com.akushch.adlershof.persistence.repository.PriceRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Configuration
@Import(DatabaseConfiguration::class)
class PersistenceConfig