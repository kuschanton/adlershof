package com.akushch.adlershof.domain.station

import arrow.core.Either
import arrow.core.right
import arrow.effects.IO
import arrow.effects.extensions.io.fx.fx

data class UpsertStationCommand(val data: StationUpsert)
data class InsertPriceCommand(val data: PriceInsert)

sealed class PriceInsertError {
    data class StationByExternalIdNotFound(val externalId: StationExternalId) : PriceInsertError()
    data class ExecutionError(val exception: Throwable) : PriceInsertError()
}

sealed class StationUpsertError {
    data class InvalidCoordinateError(val message: String) : StationUpsertError()
    data class ExecutionUpsertError(val exception: Throwable) : StationUpsertError()
}

interface UpsertStationUseCase {
    val insertStation: InsertStation
    val findByExternalId: FindByExternalId
    val validateStationInsert: ValidateStationInsert

    fun UpsertStationCommand.runUseCase(): IO<Either<StationUpsertError, Station>> = fx {
        findByExternalId(data.externalId).bind().fold(
            {
                validateStationInsert(data)
                    .map {
                        insertStation(it).bind()
                    }
            },
            { it.right() }
        )
    }
}

interface InsertPriceUseCase {
    val validateInsert: ValidatePriceInsert
    val insertPrice: InsertPrice

    fun InsertPriceCommand.runUseCase(): IO<Either<PriceInsertError, Price>> = fx {
        validateInsert(data).bind()
            .map { insertPrice(it).bind() }
    }
}

