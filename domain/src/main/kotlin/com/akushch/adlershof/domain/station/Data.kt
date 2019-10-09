package com.akushch.adlershof.domain.station

import com.akushch.adlershof.domain.subscription.Area
import java.time.Instant
import java.util.UUID

data class StationId(val value: UUID)
fun UUID.stationId() = StationId(this)

data class StationExternalId(val value: UUID)
fun UUID.stationExternalId() = StationExternalId(this)

data class Station(
    val id: StationId,
    val externalId: StationExternalId,
    val name: String,
    val brand: String,
    val place: String,
    val street: String,
    val lon: Longitude,
    val lat: Latitude,
    val houseNumber: String,
    val postCode: Int
)

data class Price(
    val stationId: StationId,
    val updateId: Long,
    val updateTimestamp: Instant,
    val diesel: Double,
    val e5: Double,
    val e10: Double
)

data class StationUpsert(
    val externalId: StationExternalId,
    val name: String,
    val brand: String,
    val place: String,
    val street: String,
    val lon: Longitude,
    val lat: Latitude,
    val houseNumber: String,
    val postCode: Int
)

data class PriceInsert(
    val externalId: StationExternalId,
    val updateId: Long,
    val updateTimestamp: Instant,
    val diesel: Double,
    val e5: Double,
    val e10: Double
)

data class ValidPriceInsert(
    val stationId: StationId,
    val updateId: Long,
    val updateTimestamp: Instant,
    val diesel: Double,
    val e5: Double,
    val e10: Double
)

/**
 * X coordinate
 */
data class Longitude(val value: Double)
fun Double.toLongitude() = Longitude(this)

/**
 * Y coordinate
 */
data class Latitude(val value: Double)
fun Double.toLatitude() = Latitude(this)

//data class StationsInAreaGet(
//    val area: Area
//)

//data class StationPriceHistory(
//    val stationId: StationId,
//    val priceHistory: List<Price>
//)