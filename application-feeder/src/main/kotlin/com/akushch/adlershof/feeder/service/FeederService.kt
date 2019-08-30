package com.akushch.adlershof.feeder.service

import arrow.core.Option
import arrow.effects.extensions.io.fx.fx
import com.akushch.adlershof.feeder.client.TankerkoenigClient
import com.akushch.adlershof.feeder.config.AreaProperties
import com.akushch.adlershof.feeder.config.CoroutinesProperties
import com.akushch.adlershof.feeder.model.AreaApi
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.asCoroutineDispatcher
import org.apache.logging.log4j.LogManager
import org.springframework.stereotype.Component
import java.util.concurrent.Executors
import kotlin.coroutines.CoroutineContext
import com.akushch.adlershof.domain.station.AddPriceToStationHistory
import com.akushch.adlershof.domain.station.GetByExternalId
import com.akushch.adlershof.domain.station.StationUpsert
import com.akushch.adlershof.domain.station.UpsertStation
import com.akushch.adlershof.domain.station.UpsertStationCommand
import com.akushch.adlershof.domain.station.UpsertStationUseCase
import com.akushch.adlershof.domain.station.stationExternalId
import com.akushch.adlershof.domain.station.stationId
import com.akushch.adlershof.domain.station.toLatitude
import com.akushch.adlershof.domain.station.toLongitude
import com.akushch.adlershof.feeder.model.StationApi
import com.akushch.adlershof.persistence.facade.PriceRepositoryFacade
import java.time.Instant
import java.util.UUID

@Component
class FeederService(
    private val tankerkoenigClient: TankerkoenigClient,
    private val priceRepositoryFacade: PriceRepositoryFacade,
    coroutinesProperties: CoroutinesProperties,
    areaProperties: AreaProperties
) {

    private val logger = LogManager.getLogger()
    private val area = areaProperties.toArea()

    private val handler = CoroutineExceptionHandler { _, throwable ->
        logger.error("Error in FeederJob", throwable)
    }
    private val dispatcher =
        Executors.newFixedThreadPool(coroutinesProperties.dispatcher.threads).asCoroutineDispatcher()
    private val coroutineContext: CoroutineContext = dispatcher + SupervisorJob() + handler

    private val useCase = object : UpsertStationUseCase {
        override val addPriceToStationHistory: AddPriceToStationHistory = priceRepositoryFacade::insertPrice
        override val getByExternalId: GetByExternalId =
            { x -> fx { logger.info("Get by external id $x"); Option.empty<com.akushch.adlershof.domain.station.Station>() } }
        override val upsertStation: UpsertStation = { x ->
            fx {
                logger.info("Upsert station $x");
                com.akushch.adlershof.domain.station.Station(
                    id = UUID.randomUUID().stationId(),
                    street = x.street,
                    postCode = x.postCode,
                    place = x.place,
                    lat = x.lat,
                    lon = x.lon,
                    houseNumber = x.houseNumber,
                    externalId = x.externalId,
                    brand = x.brand,
                    name = x.name
                )
            }
        }
    }

    fun updateStations() =
        fx {
            val commands = getStations().bind().toUpsertCommands()
            useCase.run {
                val effects = commands.map { it.runUseCase().attempt() }
                coroutineContext.parSequence(effects).bind()
            }
        }

    private fun List<StationApi>.toUpsertCommands(): List<UpsertStationCommand> {
        val updateId = System.currentTimeMillis() / 1000
        val updateTimestamp = Instant.now()
        return this.map { it.toUpsertStationCommand(updateId, updateTimestamp) }
    }

    private fun getStations() =
        tankerkoenigClient.getStationsInArea(area).map { it.stations }

    private fun AreaProperties.toArea() = AreaApi(lon.toLongitude(), lat.toLatitude(), radius)
    private fun StationApi.toUpsertStationCommand(
        updateId: Long,
        updateTimestamp: Instant
    ) = UpsertStationCommand(
        StationUpsert(
            externalId = id.stationExternalId(),
            name = name,
            brand = brand,
            updateId = updateId,
            houseNumber = houseNumber,
            lon = lng.toLongitude(),
            lat = lat.toLatitude(),
            place = place,
            postCode = postCode,
            priceDiesel = diesel,
            priceE5 = e5,
            priceE10 = e10,
            street = street,
            updateTimestamp = updateTimestamp
        )
    )

}
