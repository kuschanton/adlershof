package com.akushch.adlershof.persistence.codec

import io.netty.buffer.ByteBuf
import io.netty.buffer.ByteBufAllocator
import io.r2dbc.postgresql.client.Parameter
import io.r2dbc.postgresql.client.Parameter.NULL_VALUE
import io.r2dbc.postgresql.codec.Codec
import io.r2dbc.postgresql.message.Format
import io.r2dbc.postgresql.message.Format.FORMAT_TEXT
import io.r2dbc.postgresql.type.PostgresqlObjectId
import io.r2dbc.postgresql.util.ByteBufUtils
import org.postgresql.geometric.PGpoint
import reactor.core.publisher.Mono

class PGPointCodec(private val byteBufAllocator: ByteBufAllocator) : Codec<PGpoint> {

    private val type = PGpoint::class.java

    override fun canEncodeNull(type: Class<*>): Boolean = this.type.isAssignableFrom(type)

    override fun canEncode(value: Any): Boolean = type.isInstance(value)

    override fun encode(value: Any): Parameter = Parameter(
        FORMAT_TEXT,
        PostgresqlObjectId.POINT.objectId,
        Mono.just(
            ByteBufUtils.encode(byteBufAllocator, (value as PGpoint).toString())
        )
    )

    override fun canDecode(dataType: Int, format: Format, type: Class<*>): Boolean =
        PostgresqlObjectId.isValid(dataType)
                && (type == Object::class.java || type.isAssignableFrom(this.type))
                && FORMAT_TEXT == format
                && PostgresqlObjectId.POINT == PostgresqlObjectId.valueOf(dataType)

    override fun decode(buffer: ByteBuf?, dataType: Int, format: Format, type: Class<out PGpoint>): PGpoint? =
        buffer?.let { PGpoint(ByteBufUtils.decode(it)) }

    override fun type(): Class<*> = type

    override fun encodeNull(): Parameter = Parameter(FORMAT_TEXT, PostgresqlObjectId.POINT.objectId, NULL_VALUE)
}