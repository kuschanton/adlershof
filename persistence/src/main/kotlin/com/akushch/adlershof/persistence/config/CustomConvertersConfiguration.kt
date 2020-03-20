package com.akushch.adlershof.persistence.config

import org.postgresql.geometric.PGpoint
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.convert.converter.Converter
import org.springframework.data.convert.WritingConverter
import java.util.UUID


@Configuration
class CustomConvertersConfiguration {

    @Bean
    fun converters(): List<Converter<*, *>?> = listOf<Converter<*, *>?>(
        PGPointWritingConverter(),
        UUIDWritingConverter()
    )

    @WritingConverter
    class PGPointWritingConverter : Converter<PGpoint, PGpoint> {
        override fun convert(source: PGpoint): PGpoint = source
    }

    @WritingConverter
    class UUIDWritingConverter : Converter<UUID, UUID> {
        override fun convert(source: UUID): UUID = source
    }

}