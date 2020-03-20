package com.akushch.adlershof.persistence.facade

import com.akushch.adlershof.domain.station.Coordinate
import com.akushch.adlershof.domain.station.ValidStationInsert
import com.akushch.adlershof.domain.station.stationExternalId
import com.akushch.adlershof.domain.station.toLatitude
import com.akushch.adlershof.domain.station.toLongitude
import com.akushch.adlershof.persistence.PersistenceTest
import com.github.database.rider.core.api.connection.ConnectionHolder
import com.github.database.rider.core.api.dataset.DataSet
import com.github.database.rider.core.api.dataset.ExpectedDataSet
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import strikt.api.expectThat
import strikt.arrow.isSome
import strikt.assertions.isEqualTo
import java.util.UUID

@PersistenceTest
class StationRepositoryFacadeTest {

    @Autowired
    private lateinit var stationRepositoryFacade: StationRepositoryFacade

    @Autowired
    @Suppress("unused")
    private lateinit var connectionHolder: ConnectionHolder

    @Test
    @DataSet("datasets/stations_initial.yml")
    fun `should find by external id`() {
        val stationExternalId = UUID.fromString("51d4b6a9-a095-1aa0-e100-80009459e032").stationExternalId()

        val result = stationRepositoryFacade.findByExternalId(stationExternalId).unsafeRunSync()

        expectThat(result)
            .isSome()
            .and {
            get { t.externalId }.isEqualTo(stationExternalId)
            get { t.name }.isEqualTo("TOTAL")
            get { t.brand }.isEqualTo("TOTAL")
            get { t.place }.isEqualTo("BERLIN")
            get { t.street }.isEqualTo("STORKOWER STR.")
            get { t.coordinate }.isEqualTo(Coordinate(1344444.0.toLongitude(), 5233333.0.toLatitude()))
            get { t.houseNumber }.isEqualTo("174")
            get { t.postCode }.isEqualTo(10369)
        }
    }

    @Test
    @DataSet("datasets/stations_initial.yml")
    fun `should return empty option if not found`() {
        val externalId = UUID.randomUUID().stationExternalId()
        val result = stationRepositoryFacade.findByExternalId(externalId).unsafeRunSync()
        assertThat(result.isEmpty()).isTrue()
    }

    @Test
    @DataSet("datasets/stations_initial.yml")
    @ExpectedDataSet("datasets/station_insert.yml", orderBy = ["external_id"], ignoreCols = ["id"])
    fun `should insert station`() {
        val stationExternalId = UUID.fromString("51d4b6a9-a095-1aa0-e100-80009459e033").stationExternalId()
        val insert = ValidStationInsert(
            externalId = stationExternalId,
            name = "ARAL",
            brand = "ARAL",
            place = "BERLIN",
            street = "ELDENAER STR.",
            coordinate = Coordinate(1311111.0.toLongitude(), 522222.0.toLatitude()),
            houseNumber = "33",
            postCode = 10247
        )

        val result = stationRepositoryFacade.insertStation(insert).unsafeRunSync()

        expectThat(result).and {
            get { externalId }.isEqualTo(stationExternalId)
            get { name }.isEqualTo("ARAL")
            get { brand }.isEqualTo("ARAL")
            get { place }.isEqualTo("BERLIN")
            get { street }.isEqualTo("ELDENAER STR.")
            get { coordinate }.isEqualTo(Coordinate(1311111.0.toLongitude(), 522222.0.toLatitude()))
            get { houseNumber }.isEqualTo("33")
            get { postCode }.isEqualTo(10247)
        }
    }
}