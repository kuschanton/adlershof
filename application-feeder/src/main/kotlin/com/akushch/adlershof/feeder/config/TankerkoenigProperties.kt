package com.akushch.adlershof.feeder.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component
import org.springframework.validation.annotation.Validated
import java.time.Duration
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Positive

@Validated
@Component
@ConfigurationProperties(prefix = "tankerkoenig")
class TankerkoenigProperties {

    val api = Api()

    class Api {
        @NotBlank
        lateinit var url: String

        @NotBlank
        lateinit var apiKey: String

//        @Positive
        lateinit var timeout: Duration

        @Positive
        var retryAttempts: Long = 3
    }
}