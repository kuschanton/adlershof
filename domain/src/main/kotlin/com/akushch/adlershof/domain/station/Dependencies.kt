package com.akushch.adlershof.domain.station

import arrow.core.Option
import arrow.effects.IO
import com.akushch.adlershof.domain.subscription.Area
import java.time.Duration

typealias UpsertStation = (StationUpsert) -> IO<Station>
typealias GetStationsInArea = (Area) -> IO<List<Station>>
typealias ExistsByExternalId = (StationExternalId) -> IO<Boolean>
typealias GetByExternalId = (StationExternalId) -> IO<Option<Station>>
typealias AddPriceToStationHistory = (Price) -> IO<Price>
typealias GetPriceHistory = (StationId, FuelType, Duration) -> IO<StationPriceHistory>