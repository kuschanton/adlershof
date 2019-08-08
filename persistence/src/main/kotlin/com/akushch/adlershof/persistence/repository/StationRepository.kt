package com.akushch.adlershof.persistence.repository

import com.akushch.adlershof.persistence.model.StationP
import org.springframework.data.r2dbc.repository.R2dbcRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface StationRepository : R2dbcRepository<StationP, UUID>