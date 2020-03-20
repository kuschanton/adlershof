package com.akushch.adlershof.common

import arrow.core.Option
import arrow.core.identity
import arrow.core.toOption
import arrow.effects.extensions.io.fx.fx
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.reactive.awaitFirstOrNull
import kotlinx.coroutines.reactive.awaitSingle
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import kotlinx.coroutines.reactive.asFlow
import org.apache.logging.log4j.LogManager
import java.time.Duration

private val logger = LogManager.getLogger()

// TODO: Split into corresponding classes

// Throws if Mono is empty
fun <T> Mono<T>.toIO() = fx {
    !effect {
        awaitSingle()
    }
}

fun <T> Mono<T>.toOptionalIO() = fx {
    !effect {
        awaitFirstOrNull().toOption()
    }
}

@ExperimentalCoroutinesApi
fun <T : Any> Flux<T>.toIO() = fx {
    !effect {
        asFlow().toList()
    }
}

inline fun <T> Option<T>.getOrElse(default: () -> T): T = fold({ default() }, ::identity)

fun CoroutineScope.scheduleAtFixedRate(
    rate: Duration,
    initialDelay: Duration = Duration.ZERO,
    jobName: String,
    block: suspend () -> Unit
): Job = launch {
    delay(initialDelay.toMillis())
    while (true) {
        launch {
            runCatching {
                block()
            }.onFailure {
                logger.error("Error in scheduled job $jobName", it)
            }
        }
        delay(rate.toMillis())
    }
}