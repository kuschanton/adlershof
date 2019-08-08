package com.akushch.adlershof.persistence.repository

import com.akushch.adlershof.domain.station.Price
import org.springframework.data.r2dbc.repository.R2dbcRepository
import org.springframework.stereotype.Repository

@Repository
interface PriceRepository : R2dbcRepository<Price, Long>