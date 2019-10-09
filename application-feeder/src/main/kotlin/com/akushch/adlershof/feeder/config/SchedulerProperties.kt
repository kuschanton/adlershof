package com.akushch.adlershof.feeder.config

import org.jetbrains.annotations.NotNull
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component
import org.springframework.validation.annotation.Validated
import java.time.Duration

@Component
@Validated
@ConfigurationProperties(prefix = "scheduler")
class SchedulerProperties {

    @NotNull
    // TODO: @Positive
    lateinit var initialDelay: Duration

    // TODO: @Positive
    lateinit var delay: Duration
}
