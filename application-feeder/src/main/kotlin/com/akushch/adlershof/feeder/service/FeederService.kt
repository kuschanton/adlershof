package com.akushch.adlershof.feeder.service

import arrow.core.Either
import arrow.core.left
import arrow.effects.ForIO
import arrow.effects.IO
import arrow.effects.extensions.io.fx.fx
import arrow.effects.typeclasses.ConcurrentCancellableContinuation
import com.akushch.adlershof.domain.station.FindByExternalId
import com.akushch.adlershof.domain.station.InsertPrice
import com.akushch.adlershof.domain.station.InsertPriceCommand
import com.akushch.adlershof.domain.station.InsertPriceUseCase
import com.akushch.adlershof.domain.station.InsertStation
import com.akushch.adlershof.domain.station.Price
import com.akushch.adlershof.domain.station.PriceInsert
import com.akushch.adlershof.domain.station.PriceInsertError
import com.akushch.adlershof.domain.station.Station
import com.akushch.adlershof.domain.station.StationUpsert
import com.akushch.adlershof.domain.station.StationUpsertError
import com.akushch.adlershof.domain.station.UpsertStationCommand
import com.akushch.adlershof.domain.station.UpsertStationUseCase
import com.akushch.adlershof.domain.station.ValidatePriceInsert
import com.akushch.adlershof.domain.station.ValidatePriceInsertService
import com.akushch.adlershof.domain.station.ValidateStationInsert
import com.akushch.adlershof.domain.station.ValidateStationInsertService
import com.akushch.adlershof.domain.station.stationExternalId
import com.akushch.adlershof.domain.station.toLatitude
import com.akushch.adlershof.domain.station.toLongitude
import com.akushch.adlershof.feeder.client.TankerkoenigClient
import com.akushch.adlershof.feeder.config.AreaProperties
import com.akushch.adlershof.feeder.config.CoroutinesProperties
import com.akushch.adlershof.feeder.model.AreaApi
import com.akushch.adlershof.feeder.model.StationApi
import com.akushch.adlershof.persistence.facade.PriceRepositoryFacade
import com.akushch.adlershof.persistence.facade.StationRepositoryFacade
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.asCoroutineDispatcher
import org.apache.logging.log4j.LogManager
import org.springframework.stereotype.Component
import java.time.Instant
import java.util.concurrent.Executors
import kotlin.coroutines.CoroutineContext

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

    private val upsertStationUseCase = object : UpsertStationUseCase {
        override val findByExternalId: FindByExternalId = stationRepositoryFacade::findByExternalId
        override val insertStation: InsertStation = stationRepositoryFacade::insertStation
        override val validateStationInsert: ValidateStationInsert = { ValidateStationInsertService().run { it.validate() } }
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
                .map { it.foldExceptions { ex -> StationUpsertError.ExecutionUpsertError(ex) } }
        }

    @JvmName("executeInsertPriceCommands")
    private suspend fun ConcurrentCancellableContinuation<ForIO, *>.execute(commands: List<InsertPriceCommand>) =
        insertPriceUseCase.run {
            val effects = commands
                .map { it.runUseCase().attempt() }
            coroutineContext.parSequence(effects).bind()
                .map { it.foldExceptions { ex -> PriceInsertError.ExecutionError(ex) } }
        }

    private fun <E,R> Either<Throwable, Either<E,R>>.foldExceptions(mapper: (Throwable) -> E): Either<E, R> =
        this.fold(
            { mapper(it).left() },
            { it }
        )

    private fun List<Either<StationUpsertError, Station>>.logStationsUpsertResult() {
        val errors = this
            .filterIsInstance<Either.Left<StationUpsertError>>()
            .map { it.a }
        if (errors.isEmpty()) {
            logger.info("Stations upsert executed successfully, number of stations $size")
        } else {
            logger.info("Stations upsert executed with errors, total: $size errors: ${errors.size}")
            errors.forEach {
                logger.error("Upsert for a station failed: $it")
            }
        }
    }

    private fun List<Either<PriceInsertError, Price>>.logPricesInsertResult() {
        val errors = this
            .filterIsInstance<Either.Left<PriceInsertError>>()
            .map { it.a }
        if (errors.isEmpty()) {
            logger.info("Price insert executed successfully, number of prices $size")
        } else {
            logger.info("Price insert executed with errors, total: $size errors: ${errors.size}")
            errors.forEach {
                logger.error("Insert for a price failed: $it")
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
                lon = lng,
                lat = lat,
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
