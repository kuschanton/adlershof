package com.akushch.adlershof.persistence.model

import org.springframework.data.annotation.Id
import java.time.Instant
import java.util.UUID

data class PriceP(
    @Id
    val id: Long? = null,
    val updateId: Long,
    val updateTimestamp: Instant,
    val fuelType: FuelTypeP,
    val price: Double
)

enum class FuelTypeP {
    E5, E10, DIESEL
}

data class Station(
    val id: UUID,
    val externalId: UUID,
    val name: String,
    val brand: String,
    val place: String,
    val street: String,
    val lat: Double,
    val lon: Double,
    val houseNumber: String,
    val postCode: Int
)