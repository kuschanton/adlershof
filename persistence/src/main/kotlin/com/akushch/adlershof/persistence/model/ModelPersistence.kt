package com.akushch.adlershof.persistence.model

import com.akushch.adlershof.domain.station.FuelType
import com.akushch.adlershof.domain.station.Price
import com.akushch.adlershof.domain.station.Station
import com.akushch.adlershof.domain.station.stationExternalId
import com.akushch.adlershof.domain.station.stationId
import com.akushch.adlershof.domain.station.toLatitude
import com.akushch.adlershof.domain.station.toLongitude
import org.postgresql.geometric.PGpoint
import org.springframework.data.annotation.Id
import java.time.Instant
import java.util.UUID

data class PriceP(
    val stationId: UUID,
    val updateId: Long,
    val updateTimestamp: Instant,
    val fuelType: FuelTypeP,
    val price: Double,
    @Id
    val id: Long? = null
)

enum class FuelTypeP {
    E5, E10, DIESEL
}

data class StationP(
    val id: UUID,
    val externalId: UUID,
    val name: String,
    val brand: String,
    val place: String,
    val street: String,
    val coordinate: PGpoint,
    val houseNumber: String,
    val postCode: Int
)

fun StationP.toStation() = Station(
    id = id.stationId(),
    externalId = externalId.stationExternalId(),
    name = name,
    brand = brand,
    place = place,
    street = street,
    lon = coordinate.lon,
    lat = coordinate.lat,
    houseNumber = houseNumber,
    postCode = postCode
)

val PGpoint.lon get() = x.toLongitude()
val PGpoint.lat get() = y.toLatitude()

fun Price.toPriceP() = PriceP(
    stationId = stationId.value,
    updateId = updateId,
    updateTimestamp = updateTimestamp,
    fuelType = fuelType.toFuelTypeP(),
    price = price
)

fun PriceP.toPrice() = Price(
    stationId.stationId(),
    updateId = updateId,
    updateTimestamp = updateTimestamp,
    fuelType = fuelType.toFuelType(),
    price = price
)

fun FuelType.toFuelTypeP() = when(this) {
    FuelType.E5 -> FuelTypeP.E5
    FuelType.E10 -> FuelTypeP.E10
    FuelType.DIESEL -> FuelTypeP.DIESEL
}

fun FuelTypeP.toFuelType() = when(this) {
    FuelTypeP.E5 -> FuelType.E5
    FuelTypeP.E10 -> FuelType.E10
    FuelTypeP.DIESEL -> FuelType.DIESEL
}