package com.akushch.adlershof.domain.station

import arrow.effects.IO
import arrow.effects.extensions.io.fx.fx

data class UpsertStationCommand(val data: StationUpsert)
data class GetStationsInAreaCommand(val data: StationsInAreaGet)

interface UpsertStationUseCase {
    val upsertStation: UpsertStation
    val getByExternalId: GetByExternalId
    val addPriceToStationHistory: AddPriceToStationHistory

    fun UpsertStationCommand.runUseCase() = fx {
        getByExternalId(data.externalId).bind()
            .fold(
                { upsertStation(data).bind() },
                { it }
            )
            .also { station ->
                addPriceToStationHistory(data.dieselPrice(station.id)).bind()
                addPriceToStationHistory(data.e10Price(station.id)).bind()
                addPriceToStationHistory(data.e5Price(station.id)).bind()
            }
    }
}

interface GetStationsInAreaUseCase {
    val getStationsInArea: GetStationsInArea

    fun GetStationsInAreaCommand.runUseCase(): IO<List<Station>> = getStationsInArea(data.area)
}



