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
) {
    companion object
}

data class StationUpsert(
    val externalId: StationExternalId,
    val name: String,
    val brand: String,
    val place: String,
    val street: String,
    val lon: Longitude,
    val lat: Latitude,
    val houseNumber: String,
    val postCode: Int,
    val priceE10: Double,
    val priceE5: Double,
    val priceDiesel: Double,
    val updateId: Long,
    val updateTimestamp: Instant
)

/**
 * X coordinate
 */
data class Longitude(val value: Double)
fun Double.toLongitude() = Longitude(this)

/**
 * X coordinate
 */
data class Latitude(val value: Double)
fun Double.toLatitude() = Latitude(this)

fun StationUpsert.dieselPrice(stationId: StationId) = Price(
    stationId,
    updateId,
    updateTimestamp,
    FuelType.DIESEL,
    priceDiesel
)

fun StationUpsert.e10Price(stationId: StationId) = Price(
    stationId,
    updateId,
    updateTimestamp,
    FuelType.E10,
    priceE10
)

fun StationUpsert.e5Price(stationId: StationId) = Price(
    stationId,
    updateId,
    updateTimestamp,
    FuelType.E5,
    priceE5
)

data class StationsInAreaGet(
    val area: Area
)

data class Price(
    val stationId: StationId,
    val updateId: Long,
    val updateTimestamp: Instant,
    val fuelType: FuelType,
    val price: Double
)

enum class FuelType {
    E5, E10, DIESEL
}

data class StationPriceHistory(
    val stationId: StationId,
    val fuelType: FuelType,
    val priceHistory: List<Price>
)