package com.akushch.adlershof.domain.station

import arrow.core.Either
import arrow.effects.IO
import arrow.effects.extensions.io.fx.fx

interface ValidatePriceInsertService {
    val findByExternalId: FindByExternalId

    fun PriceInsert.validate(): IO<Either<PriceInsertError, ValidPriceInsert>> =
        fx {
            findByExternalId(externalId).bind()
                .toEither { PriceInsertError.StationByExternalIdNotFound(externalId) }
                .map {
                    ValidPriceInsert(
                        it.id,
                        updateId,
                        updateTimestamp,
                        diesel,
                        e5,
                        e10
                    )
                }
        }
}
