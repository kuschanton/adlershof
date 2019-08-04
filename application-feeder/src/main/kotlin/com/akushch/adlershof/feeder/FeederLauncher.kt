package com.akushch.adlershof.feeder

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class FeederLauncher

fun main(args: Array<String>) {
    runApplication<FeederLauncher>(*args)
}
