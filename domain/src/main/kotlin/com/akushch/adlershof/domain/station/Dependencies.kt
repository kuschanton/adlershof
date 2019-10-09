package com.akushch.adlershof.domain.station

import arrow.core.Either
import arrow.core.Option
import arrow.effects.IO

typealias UpsertStation = (StationUpsert) -> IO<Station>
//typealias GetStationsInArea = (Area) -> IO<List<Station>>
//typealias ExistsByExternalId = (StationExternalId) -> IO<Boolean>
typealias FindByExternalId = (StationExternalId) -> IO<Option<Station>>
typealias InsertPrice = (ValidPriceInsert) -> IO<Price>
typealias ValidatePriceInsert = (PriceInsert) -> IO<Either<PriceInsertError, ValidPriceInsert>>
//typealias GetPriceHistory = (StationId, Duration) -> IO<StationPriceHistory>