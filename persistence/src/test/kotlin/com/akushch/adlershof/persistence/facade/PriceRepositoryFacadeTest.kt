package com.akushch.adlershof.persistence.facade

import com.akushch.adlershof.domain.station.FuelType
import com.akushch.adlershof.domain.station.Price
import com.akushch.adlershof.domain.station.stationId
import com.github.database.rider.core.api.configuration.DBUnit
import com.github.database.rider.core.api.connection.ConnectionHolder
import com.github.database.rider.core.api.dataset.DataSet
import com.github.database.rider.core.api.dataset.ExpectedDataSet
import com.github.database.rider.junit5.api.DBRider
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.dao.DuplicateKeyException
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.testcontainers.containers.DockerComposeContainer
import org.testcontainers.junit.jupiter.Testcontainers
import java.io.File
import java.time.Instant
import java.util.UUID

@ExtendWith(SpringExtension::class)
@SpringBootTest
@ActiveProfiles(profiles = ["test"])
@DBRider
@DBUnit(caseSensitiveTableNames = true)
@Testcontainers
class PriceRepositoryFacadeTest {

    @Autowired
    private lateinit var priceRepositoryFacade: PriceRepositoryFacade

    @Autowired
    @Suppress("unused")
    private lateinit var connectionHolder: ConnectionHolder

    private val stationId1 = UUID.fromString("5cefce28-9c3a-4882-ae0a-0e7a0855ed82").stationId()
    private val updateId = 1565558887L
    private val updateTimestamp = Instant.ofEpochMilli(1565558887001)
    private val priceDiesel = 1.16

        @Test
        @DataSet("datasets/prices_initial.yml")
        @ExpectedDataSet("datasets/successful_insert.yml")
        fun `should insert successfully`() {

            val price = Price(
                stationId1,
                updateId,
                updateTimestamp,
                FuelType.DIESEL,
                priceDiesel
            )

            val result = priceRepositoryFacade.insertPrice(price).unsafeRunSync()
        }

        @Test
        @DataSet("datasets/successful_insert.yml")
        @ExpectedDataSet("datasets/successful_insert.yml")
        fun `should throw on duplicate insert`() {

            val price = Price(
                stationId1,
                updateId,
                updateTimestamp,
                FuelType.DIESEL,
                priceDiesel
            )

            assertThrows<DuplicateKeyException> { priceRepositoryFacade.insertPrice(price).unsafeRunSync() }
        }

    companion object {
        private val instance: KDockerComposeContainer by lazy { defineDockerCompose() }

        class KDockerComposeContainer(file: File) : DockerComposeContainer<KDockerComposeContainer>(file)

        private fun defineDockerCompose() = KDockerComposeContainer(File("src/test/resources/docker-compose.yml"))

        @BeforeAll
        @JvmStatic
        internal fun beforeAll() {
            instance.start()
        }

        @AfterAll
        @JvmStatic
        internal fun afterAll() {
            instance.stop()
        }
    }

}