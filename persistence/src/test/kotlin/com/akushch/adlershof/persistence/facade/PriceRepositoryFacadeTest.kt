package com.akushch.adlershof.persistence.facade

import com.akushch.adlershof.domain.station.FuelType
import com.akushch.adlershof.domain.station.Price
import com.akushch.adlershof.domain.station.stationId
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.dao.DuplicateKeyException
import org.springframework.test.context.ActiveProfiles
import org.testcontainers.containers.DockerComposeContainer
import org.testcontainers.junit.jupiter.Testcontainers
import java.io.File
import java.time.Instant
import java.util.UUID
import javax.sql.DataSource

@Testcontainers
@SpringBootTest
@ActiveProfiles(profiles = ["test"])
class PriceRepositoryFacadeTest {

    @Autowired
    private lateinit var priceRepositoryFacade: PriceRepositoryFacade

    private val stationId1 = UUID.randomUUID().stationId()
    private val updateId = System.currentTimeMillis() / 1000
    private val updateTimestamp = Instant.now()
    private val priceDiesel = 1.16

    @Nested
    inner class Insert {

        @Test
        // Add db srtate verification
        fun `should fail on double insert`() {

            val price = Price(
                stationId1,
                updateId,
                updateTimestamp,
                FuelType.DIESEL,
                priceDiesel
            )

            val result = priceRepositoryFacade.insertPrice(price).unsafeRunSync()
            assertThrows<DuplicateKeyException> { priceRepositoryFacade.insertPrice(price).unsafeRunSync() }
        }
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