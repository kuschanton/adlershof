package com.akushch.adlershof.feeder.model

import java.util.UUID
import com.akushch.adlershof.domain.station.Latitude
import com.akushch.adlershof.domain.station.Longitude

data class AreaApi(
    val lon: Longitude,
    val lat: Latitude,
    val radius: Int
)

data class StationApi (
    val id : UUID,
    val name : String,
    val brand : String,
    val street : String,
    val place : String,
    val lat : Double,
    val lng : Double,
    val dist : Double,
    val diesel : Double,
    val e5 : Double,
    val e10 : Double,
    val isOpen : Boolean,
    val houseNumber : String,
    val postCode : Int
)

class TankerkoenigResponse(
    val ok: Boolean,
    val license: String,
    val data: String,
    val status: String,
    val stations: List<StationApi>
)