package com.akushch.adlershof.common

import arrow.effects.extensions.io.fx.fx
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.reactive.awaitSingle
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import kotlinx.coroutines.reactive.flow.asFlow

fun <T> Mono<T>.toIO() = fx {
    !effect {
        awaitSingle()
    }
}

fun <T : Any> Flux<T>.toIO() = fx {
    !effect {
        asFlow().toList()
    }
}