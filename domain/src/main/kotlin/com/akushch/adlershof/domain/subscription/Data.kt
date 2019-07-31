package com.akushch.adlershof.domain.subscription

import java.util.UUID

data class Subscription(
    val id: UUID,
    val userId: UUID,
    val area: Area,
    val active: Boolean,
    val name: String,
    val ordinal: Int
)

data class Area(
    val lat: Double,
    val lon: Double,
    val radius: Int
)

data class SubscriptionCreation(
    val area: Area,
    val name: String
)

data class ValidSubscriptionCreation(
    val id: UUID,
    val area: Area,
    val name: String
)