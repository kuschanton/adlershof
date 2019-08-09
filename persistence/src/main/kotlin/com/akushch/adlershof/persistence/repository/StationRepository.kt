package com.akushch.adlershof.persistence.repository

import com.akushch.adlershof.persistence.model.StationP
import org.springframework.data.r2dbc.repository.R2dbcRepository
import org.springframework.data.r2dbc.repository.query.Query
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import java.util.UUID

@Repository
interface StationRepository : R2dbcRepository<StationP, UUID> {

    @Query("SELECT * FROM station WHERE external_id = $1")
    fun findByExternalId(externalId: UUID): Flux<StationP>
}