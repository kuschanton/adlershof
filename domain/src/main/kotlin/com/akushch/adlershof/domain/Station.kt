package com.akushch.adlershof.domain

import java.util.UUID

data class Station(
    val id: UUID,
    val name: String,
    val brand: String,
    val place: String,
    val street: String,
    val lat: Double,
    val lon: Double,
    val houseNumber: String,
    val postCode: Int
)