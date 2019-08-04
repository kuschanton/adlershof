package com.akushch.adlershof.feeder.config

import org.jetbrains.annotations.NotNull
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component
import org.springframework.validation.annotation.Validated
import java.time.Duration
import javax.validation.constraints.Positive

@Component
@Validated
@ConfigurationProperties(prefix = "scheduler")
class SchedulerProperties {

    @NotNull
//    @Positive
    lateinit var initialDelay: Duration

//    @Positive
    lateinit var delay: Duration
}
