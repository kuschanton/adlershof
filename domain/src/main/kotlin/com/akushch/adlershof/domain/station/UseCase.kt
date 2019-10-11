package com.akushch.adlershof.domain.station

import arrow.effects.extensions.io.fx.fx
import com.akushch.adlershof.common.getOrElse

data class UpsertStationCommand(val data: StationUpsert)
data class InsertPriceCommand(val data: PriceInsert)
//data class GetStationsInAreaCommand(val data: StationsInAreaGet)

sealed class PriceInsertError {
    data class StationByExternalIdNotFound(val externalId: StationExternalId) : PriceInsertError()
    data class ExecutionError(val exception: Throwable) : PriceInsertError()
}

sealed class UpsertStationError {
    data class ExecutionError(val exception: Throwable) : UpsertStationError()
}

interface UpsertStationUseCase {
    val upsertStation: UpsertStation
    val findByExternalId: FindByExternalId

    fun UpsertStationCommand.runUseCase() = fx {
        findByExternalId(data.externalId).bind()
            .getOrElse { upsertStation(data).bind() }
    }
}

interface InsertPriceUseCase {
    val validateInsert: ValidatePriceInsert
    val insertPrice: InsertPrice

    fun InsertPriceCommand.runUseCase() = fx {
        validateInsert(data).bind()
            .map { insertPrice(it).bind() }
    }
}

//interface GetStationsInAreaUseCase {
//    val getStationsInArea: GetStationsInArea
//    fun GetStationsInAreaCommand.runUseCase(): IO<List<Station>> = getStationsInArea(data.area)
//}



