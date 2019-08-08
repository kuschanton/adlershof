package com.akushch.adlershof.persistence.repository

import com.akushch.adlershof.persistence.model.PriceP
import org.springframework.data.r2dbc.repository.R2dbcRepository
import org.springframework.stereotype.Repository

@Repository
interface PriceRepository : R2dbcRepository<PriceP, Long>