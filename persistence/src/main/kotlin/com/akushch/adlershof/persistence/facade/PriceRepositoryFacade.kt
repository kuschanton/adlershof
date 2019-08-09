package com.akushch.adlershof.persistence.facade

import com.akushch.adlershof.common.toIO
import com.akushch.adlershof.domain.station.Price
import com.akushch.adlershof.domain.station.StationExternalId
import com.akushch.adlershof.persistence.model.toPrice
import com.akushch.adlershof.persistence.model.toPriceP
import com.akushch.adlershof.persistence.model.toStation
import com.akushch.adlershof.persistence.repository.PriceRepository
import com.akushch.adlershof.persistence.repository.StationRepository
import org.springframework.stereotype.Repository

@Repository
class PriceRepositoryFacade(
    val priceRepository: PriceRepository,
    val stationRepository: StationRepository
) {
    fun addPriceToStation(price: Price) =
        price.let { it.toPriceP() }
            .let { priceRepository.save(it) }
            .map { it.toPrice() }
            .toIO()

    fun getByExternalId(stationExternalId: StationExternalId) =
        stationRepository.findByExternalId(stationExternalId.value)
            .map { it.toStation() }
            .toIO()

}