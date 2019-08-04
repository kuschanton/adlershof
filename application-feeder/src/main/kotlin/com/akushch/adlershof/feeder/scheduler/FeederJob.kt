package com.akushch.adlershof.feeder.scheduler

import arrow.effects.extensions.eithert.async.delay
import com.akushch.adlershof.feeder.client.TankerkoenigClient
import com.akushch.adlershof.feeder.config.SchedulerProperties
import com.akushch.adlershof.feeder.service.FeederService
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.apache.logging.log4j.LogManager
import org.springframework.stereotype.Component
import java.time.Duration
import java.util.concurrent.Executors
import javax.annotation.PostConstruct
import kotlin.coroutines.CoroutineContext

@Component
class FeederJob(
    val feederService: FeederService,
    val schedulerProperties: SchedulerProperties
) : CoroutineScope {

    private val logger = LogManager.getLogger()

    override val coroutineContext: CoroutineContext = Job() + Dispatchers.IO

    @PostConstruct
    fun initFeederJob() {
        logger.info("Initializing feeder job")
        scheduleAtFixedRate(schedulerProperties.delay, schedulerProperties.initialDelay) {
            feederService.updateStations()
                .unsafeRunAsync { result ->
                    result.fold(
                        { logger.error("Update failed", it) },
                        { logger.info("Update executed successfully, number of stations ${it.size}") }
                    )
                }
        }
    }

    fun scheduleAtFixedRate(
        rate: Duration,
        initialDelay: Duration = Duration.ZERO,
        block: suspend () -> Unit
    ): Job = launch {
        delay(initialDelay.toMillis())
        var firstLaunch = true
        while (true) {
            if (firstLaunch) {
                firstLaunch = false
            } else {
                delay(rate.toMillis())
            }
            launch {
                runCatching {
                    block()
                }.onFailure {
                    logger.error("Error in feeder scheduled job", it)
                }
            }
        }
    }

}