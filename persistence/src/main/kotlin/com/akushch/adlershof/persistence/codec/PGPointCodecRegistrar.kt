package com.akushch.adlershof.persistence.codec

import io.netty.buffer.ByteBufAllocator
import io.r2dbc.postgresql.api.PostgresqlConnection
import io.r2dbc.postgresql.codec.CodecRegistry
import io.r2dbc.postgresql.extension.CodecRegistrar
import org.reactivestreams.Publisher
import reactor.core.publisher.Mono

class PGPointCodecRegistrar : CodecRegistrar {
    override fun register(
        connection: PostgresqlConnection,
        allocator: ByteBufAllocator,
        registry: CodecRegistry
    ): Publisher<Void> = Mono.just(PGPointCodec(allocator))
        .doOnNext { registry.addLast(it) }
        .then()
}
