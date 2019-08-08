package com.akushch.adlershof.persistence.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.validation.annotation.Validated
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Positive

@Validated
@ConfigurationProperties(prefix = "spring.data.postgres")
class DatabaseProperties {
    @NotBlank
    lateinit var host: String

    @Positive
    var port: Int = 5432

    @NotBlank
    lateinit var database: String

    @NotBlank
    lateinit var username: String

    @NotBlank
    lateinit var password: String
}