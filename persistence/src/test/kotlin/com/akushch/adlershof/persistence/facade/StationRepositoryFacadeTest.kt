package com.akushch.adlershof.persistence.facade

import arrow.core.some
import com.akushch.adlershof.domain.station.Station
import com.akushch.adlershof.domain.station.stationExternalId
import com.akushch.adlershof.domain.station.stationId
import com.akushch.adlershof.domain.station.toLatitude
import com.akushch.adlershof.domain.station.toLongitude
import com.akushch.adlershof.persistence.config.PostgresqlDataTypeFactory
import com.github.database.rider.core.api.configuration.DBUnit
import com.github.database.rider.core.api.connection.ConnectionHolder
import com.github.database.rider.core.api.dataset.DataSet
import com.github.database.rider.junit5.api.DBRider
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.testcontainers.containers.DockerComposeContainer
import org.testcontainers.junit.jupiter.Testcontainers
import java.io.File
import java.util.UUID

@ExtendWith(SpringExtension::class)
@SpringBootTest
@ActiveProfiles(profiles = ["test"])
@DBRider
@DBUnit(
    caseSensitiveTableNames = true,
    dataTypeFactoryClass = PostgresqlDataTypeFactory::class
)
@Testcontainers
class StationRepositoryFacadeTest {

    @Autowired
    private lateinit var stationRepositoryFacade: StationRepositoryFacade

    @Autowired
    @Suppress("unused")
    private lateinit var connectionHolder: ConnectionHolder

    private val externalId = UUID.fromString("474e5046-deaf-4f9b-9a32-9797b778f047").stationExternalId()

    @Test
    @DataSet("datasets/stations_initial.yml")
    fun `should find by external id`() {
        val expected = Station(
            id = UUID.fromString("4429a7d9-fb2d-4c29-8cfe-2ca90323f9f8").stationId(),
            externalId = UUID.fromString("474e5046-deaf-4f9b-9a32-9797b778f047").stationExternalId(),
            name = "TOTAL",
            brand = "TOTAL",
            place = "BERLIN",
            street = "STORKOWER STR.",
            lon = 52.33333.toLongitude(),
            lat = 13.44444.toLatitude(),
            houseNumber = "174",
            postCode = 10369
        )
        val result = stationRepositoryFacade.findByExternalId(externalId).unsafeRunSync()
        assertThat(result).isEqualTo(expected.some())
    }

    @Test
    @DataSet("datasets/stations_initial.yml")
    fun `should return empty option if not found`() {
        val externalId = UUID.fromString("4429a7d9-fb2d-4c29-8cfe-1111111111111111").stationExternalId()
        val result = stationRepositoryFacade.findByExternalId(externalId).unsafeRunSync()
        assertThat(result.isEmpty()).isTrue()
    }

    companion object {
//        private val instance: KDockerComposeContainer by lazy { defineDockerCompose() }
//
//        class KDockerComposeContainer(file: File) : DockerComposeContainer<KDockerComposeContainer>(file)
//
//        private fun defineDockerCompose() = KDockerComposeContainer(File("src/test/resources/docker-compose.yml"))
//
//        @BeforeAll
//        @JvmStatic
//        internal fun beforeAll() {
//            instance.start()
//        }
//
//        @AfterAll
//        @JvmStatic
//        internal fun afterAll() {
//            instance.stop()
//        }
    }

}