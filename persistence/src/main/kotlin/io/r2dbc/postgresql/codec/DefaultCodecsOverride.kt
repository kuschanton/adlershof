package io.r2dbc.postgresql.codec

import io.netty.buffer.ByteBuf
import io.netty.buffer.ByteBufAllocator
import io.r2dbc.postgresql.client.Parameter
import io.r2dbc.postgresql.message.Format


// TODO: To workaround r2dbc limitation, remove after creating custom codecs is possible
internal class DefaultCodecsOverride(
    byteBufAllocator: ByteBufAllocator,
    customCodecs: List<Codec<*>>
) : Codecs {

    private val codecs : List<Codec<*>> = customCodecs + arrayListOf(

        // Prioritized Codecs
        ShortCodec(byteBufAllocator),
        StringCodec(byteBufAllocator),
        InstantCodec(byteBufAllocator),
        ZonedDateTimeCodec(byteBufAllocator),

        BigDecimalCodec(byteBufAllocator),
        BooleanCodec(byteBufAllocator),
        ByteCodec(byteBufAllocator),
        CharacterCodec(byteBufAllocator),
        DateCodec(byteBufAllocator),
        DoubleCodec(byteBufAllocator),
        EnumCodec(byteBufAllocator),
        FloatCodec(byteBufAllocator),
        InetAddressCodec(byteBufAllocator),
        IntegerCodec(byteBufAllocator),
        LocalDateCodec(byteBufAllocator),
        LocalDateTimeCodec(byteBufAllocator),
        LocalTimeCodec(byteBufAllocator),
        LongCodec(byteBufAllocator),
        OffsetDateTimeCodec(byteBufAllocator),
        UriCodec(byteBufAllocator),
        UrlCodec(byteBufAllocator),
        UuidCodec(byteBufAllocator),
        ZoneIdCodec(byteBufAllocator),

        ShortArrayCodec(byteBufAllocator),
        StringArrayCodec(byteBufAllocator),
        IntegerArrayCodec(byteBufAllocator),
        LongArrayCodec(byteBufAllocator)
    )

    override fun preferredType(dataType: Int, format: Format): Class<*>? =
        codecs.firstOrNull { it.canDecode(dataType, format, Any::class.java) }?.type()

    override fun encode(value: Any): Parameter =
        codecs.firstOrNull { it.canEncode(value) }?.encode(value)
            ?: throw IllegalArgumentException("Cannot encode parameter of type ${value.javaClass.name}")

    override fun <T : Any?> decode(byteBuf: ByteBuf?, dataType: Int, format: Format, type: Class<out T>): T? =
        byteBuf?.let {
            codecs.firstOrNull { it.canDecode(dataType, format, type) }
                ?.let { it as Codec<T> }
                ?.decode(byteBuf, format, type)
                ?: throw IllegalArgumentException("Cannot decode value of type ${type.name}");
        }

    override fun encodeNull(type: Class<*>): Parameter =
        codecs.firstOrNull { it.canEncodeNull(type) }?.encodeNull()
            ?: throw IllegalArgumentException("Cannot encode null parameter of type ${type.name}")
}