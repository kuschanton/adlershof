package com.akushch.adlershof.feeder.scheduler

import com.akushch.adlershof.common.scheduleAtFixedRate
import com.akushch.adlershof.feeder.config.SchedulerProperties
import com.akushch.adlershof.feeder.service.FeederService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import org.apache.logging.log4j.LogManager
import org.springframework.stereotype.Component
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
        scheduleAtFixedRate(
            rate = schedulerProperties.delay,
            jobName = "FeederJob",
            initialDelay = schedulerProperties.initialDelay
        ) {
            feederService.updateStations()
                .unsafeRunAsync { result ->
                    result.fold(
                        { logger.error("Update failed", it) },
                        { it }
                    )
                }
        }
    }
}