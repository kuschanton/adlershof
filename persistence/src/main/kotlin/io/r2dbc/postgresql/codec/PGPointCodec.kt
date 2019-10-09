package io.r2dbc.postgresql.codec

import io.netty.buffer.ByteBuf
import io.netty.buffer.ByteBufAllocator
import io.r2dbc.postgresql.message.Format
import io.r2dbc.postgresql.message.Format.FORMAT_TEXT
import io.r2dbc.postgresql.type.PostgresqlObjectId
import io.r2dbc.postgresql.util.ByteBufUtils
import org.postgresql.geometric.PGpoint

internal class PGPointCodec(private val byteBufAllocator: ByteBufAllocator)
    : AbstractCodec<PGpoint>(PGpoint::class.java) {

    override fun doDecode(byteBuf: ByteBuf, format: Format, type: Class<out PGpoint>): PGpoint {
        return PGpoint(ByteBufUtils.decode(byteBuf))
    }

    override fun encodeNull() = createNull(FORMAT_TEXT, PostgresqlObjectId.POINT)

    override fun doCanDecode(format: Format, type: PostgresqlObjectId) =
        FORMAT_TEXT == format && PostgresqlObjectId.POINT == type

    override fun doEncode(value: PGpoint) = create(
        FORMAT_TEXT,
        PostgresqlObjectId.UUID,
        ByteBufUtils.encode(byteBufAllocator, value.toString())
    )
}