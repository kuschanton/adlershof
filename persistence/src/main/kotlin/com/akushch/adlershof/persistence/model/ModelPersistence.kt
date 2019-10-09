package com.akushch.adlershof.persistence.model

import com.akushch.adlershof.domain.station.Price
import com.akushch.adlershof.domain.station.Station
import com.akushch.adlershof.domain.station.ValidPriceInsert
import com.akushch.adlershof.domain.station.stationExternalId
import com.akushch.adlershof.domain.station.stationId
import com.akushch.adlershof.domain.station.toLatitude
import com.akushch.adlershof.domain.station.toLongitude
import org.postgresql.geometric.PGpoint
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
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

data class StationP(
    val id: UUID,
    val externalId: UUID,
    val name: String,
    val brand: String,
    val place: String,
    val street: String,
    val coordinate: String,
    val houseNumber: String,
    val postCode: Int
)

fun StationP.toStation() = with(PGpoint(coordinate)) {
    Station(
        id = id.stationId(),
        externalId = externalId.stationExternalId(),
        name = name,
        brand = brand,
        place = place,
        street = street,
        lon = lon,
        lat = lat,
        houseNumber = houseNumber,
        postCode = postCode
    )
}

val PGpoint.lon get() = x.toLongitude()
val PGpoint.lat get() = y.toLatitude()

fun ValidPriceInsert.toPriceP() = PriceP(
    stationId = stationId.value,
    updateId = updateId,
    updateTimestamp = updateTimestamp,
    diesel = diesel,
    e5 = e5,
    e10 = e10
)

fun PriceP.toPrice() = Price(
    stationId.stationId(),
    updateId = updateId,
    updateTimestamp = updateTimestamp,
    diesel = diesel,
    e5 = e5,
    e10 = e10
)