package com.akushch.adlershof.persistence.facade

import arrow.core.Option
import arrow.effects.IO
import com.akushch.adlershof.common.toIO
import com.akushch.adlershof.common.toOptionalIO
import com.akushch.adlershof.domain.station.Station
import com.akushch.adlershof.domain.station.StationExternalId
import com.akushch.adlershof.domain.station.ValidStationInsert
import com.akushch.adlershof.persistence.model.toStation
import com.akushch.adlershof.persistence.model.toStationP
import com.akushch.adlershof.persistence.repository.StationRepository
import org.springframework.stereotype.Repository

@Repository
class StationRepositoryFacade(
    private val stationRepository: StationRepository
) {

    fun findByExternalId(stationExternalId: StationExternalId): IO<Option<Station>> =
        stationRepository.findByExternalId(stationExternalId.value)
            .singleOrEmpty()
            .map { it.toStation() }
            .toOptionalIO()

    fun insertStation(stationInsert: ValidStationInsert): IO<Station> =
        stationRepository.save(stationInsert.toStationP())
            .map { it.toStation() }
            .toIO()
}