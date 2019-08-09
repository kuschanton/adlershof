package com.akushch.adlershof.domain.subscription

import com.akushch.adlershof.domain.station.Latitude
import com.akushch.adlershof.domain.station.Longitude
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
    val lon: Longitude,
    val lat: Latitude,
    val radius: Int
)

data class SubscriptionCreation(
    val area: Area,
    val name: String
)

data class SubscriptionDeletion(
    val id: UUID
)

data class ValidSubscriptionCreation(
    val id: UUID,
    val area: Area,
    val name: String
)