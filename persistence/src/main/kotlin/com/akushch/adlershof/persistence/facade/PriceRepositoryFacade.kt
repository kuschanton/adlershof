package com.akushch.adlershof.persistence.facade

import com.akushch.adlershof.common.toIO
import com.akushch.adlershof.domain.station.ValidPriceInsert
import com.akushch.adlershof.persistence.model.toPrice
import com.akushch.adlershof.persistence.model.toPriceP
import com.akushch.adlershof.persistence.repository.PriceRepository
import org.springframework.stereotype.Repository

@Repository
class PriceRepositoryFacade(
    private val priceRepository: PriceRepository
) {
    fun insertPrice(priceInsert: ValidPriceInsert) =
            priceRepository.save(priceInsert.toPriceP())
            .map { it.toPrice() }
            .toIO()
}