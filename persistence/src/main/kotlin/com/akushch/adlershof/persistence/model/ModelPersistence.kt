package com.akushch.adlershof.persistence.model

import com.akushch.adlershof.domain.station.Coordinate
import com.akushch.adlershof.domain.station.Price
import com.akushch.adlershof.domain.station.Station
import com.akushch.adlershof.domain.station.ValidPriceInsert
import com.akushch.adlershof.domain.station.ValidStationInsert
import com.akushch.adlershof.domain.station.stationExternalId
import com.akushch.adlershof.domain.station.stationId
import com.akushch.adlershof.domain.station.toLatitude
import com.akushch.adlershof.domain.station.toLongitude
import org.postgresql.geometric.PGpoint
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.lang.IllegalArgumentException
import java.time.Instant
import java.util.UUID

@Table("price")
data class PriceP(
    val stationId: UUID,
    val updateId: Long,
    val updateTimestamp: Instant,
    val diesel: Double,
    val e5: Double,
    val e10: Double,
    @Id
    val id: Long? = null
)

@Table("station")
data class StationP(
    val externalId: UUID,
    val name: String,
    val brand: String,
    val place: String,
    val street: String,
    val coordinate: PGpoint,
    val houseNumber: String,
    val postCode: Int,
    @Id
    val id: UUID? = null
)

fun StationP.toStation() =
    Station(
        id = id.stationId(),
        externalId = externalId.stationExternalId(),
        name = name,
        brand = brand,
        place = place,
        street = street,
        coordinate = Coordinate(coordinate.lon, coordinate.lat),
        houseNumber = houseNumber,
        postCode = postCode
    )

private fun UUID?.stationId() = (this ?: throw IllegalArgumentException("Station id is null"))
    .stationId()

fun ValidPriceInsert.toPriceP() = PriceP(
    stationId = stationId.value,
    updateId = updateId,
    updateTimestamp = updateTimestamp,
    diesel = diesel,
    e5 = e5,
    e10 = e10
)

fun ValidStationInsert.toStationP() = StationP(
    externalId = externalId.value,
    name = name,
    brand = brand,
    place = place,
    street = street,
    coordinate = coordinate.toPGPoint(),
    houseNumber = houseNumber,
    postCode = postCode
)

fun Coordinate.toPGPoint() = PGpoint(xLongitude.value, yLatitude.value)
val PGpoint.lon get() = x.toLongitude()
val PGpoint.lat get() = y.toLatitude()

fun PriceP.toPrice() = Price(
    stationId.stationId(),
    updateId = updateId,
    updateTimestamp = updateTimestamp,
    diesel = diesel,
    e5 = e5,
    e10 = e10
)