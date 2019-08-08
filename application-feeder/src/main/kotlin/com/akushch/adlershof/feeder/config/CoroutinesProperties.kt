package com.akushch.adlershof.feeder.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component
import org.springframework.validation.annotation.Validated
import javax.validation.constraints.Max
import javax.validation.constraints.Positive

@Component
@Validated
@ConfigurationProperties(prefix = "coroutines")
class CoroutinesProperties {

    val dispatcher = Dispatcher()

    class Dispatcher {
        @Positive
        @Max(20)
        var threads: Int = 4
    }
}