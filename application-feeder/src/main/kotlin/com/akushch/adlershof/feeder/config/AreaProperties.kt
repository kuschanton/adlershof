package com.akushch.adlershof.feeder.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component
import org.springframework.validation.annotation.Validated
import javax.validation.constraints.Max
import javax.validation.constraints.Positive

@Validated
@Component
@ConfigurationProperties(prefix = "area")
class AreaProperties {

    @Positive
    @Max(180)
    var lat: Double = 52.52

    @Positive
    @Max(180)
    var lon: Double = 13.43

    @Positive
    var radius: Int = 5
}