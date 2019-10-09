package com.akushch.adlershof.feeder.service

import arrow.core.Either
import arrow.effects.ForIO
import arrow.effects.IO
import arrow.effects.extensions.io.fx.fx
import arrow.effects.typeclasses.ConcurrentCancellableContinuation
import com.akushch.adlershof.domain.station.InsertPrice
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
import com.akushch.adlershof.domain.station.FindByExternalId
import com.akushch.adlershof.domain.station.InsertPriceCommand
import com.akushch.adlershof.domain.station.InsertPriceUseCase
import com.akushch.adlershof.domain.station.Price
import com.akushch.adlershof.domain.station.PriceInsert
import com.akushch.adlershof.domain.station.ValidPriceInsert
import com.akushch.adlershof.domain.station.PriceInsertError
import com.akushch.adlershof.domain.station.Station
import com.akushch.adlershof.domain.station.StationUpsert
import com.akushch.adlershof.domain.station.UpsertStation
import com.akushch.adlershof.domain.station.UpsertStationCommand
import com.akushch.adlershof.domain.station.UpsertStationUseCase
import com.akushch.adlershof.domain.station.ValidatePriceInsert
import com.akushch.adlershof.domain.station.ValidatePriceInsertService
import com.akushch.adlershof.domain.station.stationExternalId
import com.akushch.adlershof.domain.station.stationId
import com.akushch.adlershof.domain.station.toLatitude
import com.akushch.adlershof.domain.station.toLongitude
import com.akushch.adlershof.feeder.model.StationApi
import com.akushch.adlershof.persistence.facade.PriceRepositoryFacade
import com.akushch.adlershof.persistence.facade.StationRepositoryFacade
import java.time.Instant
import java.util.UUID

@Component
class FeederService(
    private val tankerkoenigClient: TankerkoenigClient,
    private val priceRepositoryFacade: PriceRepositoryFacade,
    private val stationRepositoryFacade: StationRepositoryFacade,
    coroutinesProperties: CoroutinesProperties,
    areaProperties: AreaProperties
) {

    private val logger = LogManager.getLogger()
    private val area = areaProperties.toArea()

    private val handler = CoroutineExceptionHandler { _, throwable ->
        logger.error("Error in FeederService", throwable)
    }
    private val dispatcher =
        Executors.newFixedThreadPool(coroutinesProperties.dispatcher.threads).asCoroutineDispatcher()

    private val coroutineContext: CoroutineContext = dispatcher + SupervisorJob() + handler

    // TODO: Remove this
    private val dummyFunction: UpsertStation = { x ->
        fx {
            logger.info("Upsert station $x")
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

    private val upsertStationUseCase = object : UpsertStationUseCase {
        override val findByExternalId: FindByExternalId = stationRepositoryFacade::findByExternalId
        override val upsertStation: UpsertStation = dummyFunction
    }

    private val insertPriceUseCase = object : InsertPriceUseCase {
        override val validateInsert: ValidatePriceInsert = { validatePriceInsertSrv.run { it.validate() } }
        override val insertPrice: InsertPrice = priceRepositoryFacade::insertPrice
    }

    private val validatePriceInsertSrv = object : ValidatePriceInsertService {
        override val findByExternalId: FindByExternalId = stationRepositoryFacade::findByExternalId
    }

    fun updateStations(): IO<Unit> =
        fx {
            val stations = getStations().bind()

            execute(stations.toStationUpsertCommands())
                .logStationsUpsertResult()
            execute(stations.toInsertPriceCommands())
                .logPricesInsertResult()
        }

    @JvmName("executeUpsertStationCommands")
    private suspend fun ConcurrentCancellableContinuation<ForIO, *>.execute(commands: List<UpsertStationCommand>) =
        upsertStationUseCase.run {
            val effects = commands
                .map { it.runUseCase().attempt() }
            coroutineContext.parSequence(effects).bind()
        }

    @JvmName("executeInsertPriceCommands")
    private suspend fun ConcurrentCancellableContinuation<ForIO, *>.execute(commands: List<InsertPriceCommand>) =
        insertPriceUseCase.run {
            val effects = commands
                .map { it.runUseCase().attempt() }
            coroutineContext.parSequence(effects).bind()
        }

    private fun List<Either<Throwable, Station>>.logStationsUpsertResult() {
        val exceptions = this
            .filterIsInstance<Either.Left<Throwable>>()
            .map { it.a }
        if (exceptions.isEmpty()) {
            logger.info("Stations upsert executed successfully, number of stations $size")
        } else {
            logger.info("Stations upsert executed with errors, total: $size errors: ${exceptions.size}")
            exceptions.forEach {
                logger.error("Upsert for a station failed", it)
            }
        }
    }

    private fun List<Either<Throwable, Either<PriceInsertError, Price>>>.logPricesInsertResult() {
        val exceptions = this
            .filterIsInstance<Either.Left<Throwable>>()
            .map { it.a }
        val errors = this
            .filterIsInstance<Either.Right<Either<PriceInsertError, Price>>>()
            .map { it.b }
            .filterIsInstance<Either.Left<PriceInsertError>>()
            .map { it.a }
        if (exceptions.isEmpty() && errors.isEmpty()) {
            logger.info("Price insert executed successfully, number of prices $size")
        } else {
            logger.info(
                "Price insert executed with errors and/or exceptions, " +
                        "total: $size, exceptions: ${exceptions.size} errors: ${errors.size}"
            )
            exceptions.forEach {
                logger.error("Price insert exceptions", it)
            }
            errors.forEach {
                logger.error("Price insert error $it")
            }
        }
    }

    private fun getStations() =
        tankerkoenigClient.getStationsInArea(area).map { it.stations }

    companion object {
        private fun List<StationApi>.toStationUpsertCommands(): List<UpsertStationCommand> {
            return this.map {
                it.toUpsertStationCommand()
            }
        }

        private fun List<StationApi>.toInsertPriceCommands(): List<InsertPriceCommand> {
            val updateId = System.currentTimeMillis() / 1000
            val updateTimestamp = Instant.now()
            return this.map {
                it.toInsertPriceCommand(updateId, updateTimestamp)
            }
        }

        private fun AreaProperties.toArea() = AreaApi(lon.toLongitude(), lat.toLatitude(), radius)

        private fun StationApi.toUpsertStationCommand() = UpsertStationCommand(
            StationUpsert(
                externalId = id.stationExternalId(),
                name = name,
                brand = brand,
                houseNumber = houseNumber,
                lon = lng.toLongitude(),
                lat = lat.toLatitude(),
                place = place,
                postCode = postCode,
                street = street
            )
        )

        private fun StationApi.toInsertPriceCommand(
            updateId: Long,
            updateTimestamp: Instant
        ) = InsertPriceCommand(
            PriceInsert(
                id.stationExternalId(),
                updateId,
                updateTimestamp,
                diesel,
                e5,
                e10
            )
        )
    }
}
