package com.akushch.adlershof.domain.station

import arrow.core.Either
import arrow.core.Option
import arrow.effects.IO

typealias InsertStation = (ValidStationInsert) -> IO<Station>
typealias ValidateStationInsert = (StationUpsert) -> Either<StationUpsertError, ValidStationInsert>
typealias FindByExternalId = (StationExternalId) -> IO<Option<Station>>
typealias InsertPrice = (ValidPriceInsert) -> IO<Price>
typealias ValidatePriceInsert = (PriceInsert) -> IO<Either<PriceInsertError, ValidPriceInsert>>