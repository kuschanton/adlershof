package com.akushch.adlershof.persistence.facade

import arrow.core.toOption
import com.akushch.adlershof.common.toIO
import com.akushch.adlershof.common.toOptionalIO
import com.akushch.adlershof.domain.station.StationExternalId
import com.akushch.adlershof.persistence.model.toStation
import com.akushch.adlershof.persistence.repository.StationRepository
import org.springframework.stereotype.Repository

@Repository
class StationRepositoryFacade(
    private val stationRepository: StationRepository
) {

    fun findByExternalId(stationExternalId: StationExternalId) =
        stationRepository.findByExternalId(stationExternalId.value)
            .singleOrEmpty()
            .map { it.toStation() }
            .toOptionalIO()
}