package com.akushch.adlershof.domain.station

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import arrow.effects.IO
import arrow.effects.extensions.io.fx.fx
import java.util.UUID

interface ValidatePriceInsertService {
    val findByExternalId: FindByExternalId

    fun PriceInsert.validate(): IO<Either<PriceInsertError, ValidPriceInsert>> =
        fx {
            findByExternalId(externalId).bind()
                .toEither { PriceInsertError.StationByExternalIdNotFound(externalId) }
                .map {
                    ValidPriceInsert(
                        stationId = it.id,
                        updateId = updateId,
                        updateTimestamp = updateTimestamp,
                        diesel = diesel,
                        e5 = e5,
                        e10 = e10
                    )
                }
        }
}

class ValidateStationInsertService {
    fun StationUpsert.validate(): Either<StationUpsertError, ValidStationInsert> =
        if (lon.isValidLongitude() && lat.isValidLatitude()) {
            ValidStationInsert(
                externalId = externalId,
                name = name,
                brand = brand,
                place = place,
                street = street,
                coordinate = Coordinate(lon.toLongitude(), lat.toLatitude()),
                houseNumber = houseNumber,
                postCode = postCode
            ).right()
        } else {
            StationUpsertError.InvalidCoordinateError("Invalid lat/lon: $lat/$lon").left()
        }

    private fun Double.isValidLongitude() = this in (-180.0..180.0)
    private fun Double.isValidLatitude() = this in (-90.0..90.0)
}

