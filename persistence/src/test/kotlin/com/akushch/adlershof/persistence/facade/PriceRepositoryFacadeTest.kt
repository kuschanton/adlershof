package com.akushch.adlershof.persistence.facade

import com.akushch.adlershof.domain.station.Price
import com.akushch.adlershof.domain.station.ValidPriceInsert
import com.akushch.adlershof.domain.station.stationId
import com.akushch.adlershof.persistence.PersistenceBaseTest
import com.akushch.adlershof.persistence.PersistenceTest
import com.github.database.rider.core.api.connection.ConnectionHolder
import com.github.database.rider.core.api.dataset.DataSet
import com.github.database.rider.core.api.dataset.ExpectedDataSet
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.test.annotation.DirtiesContext
import java.time.Instant
import java.util.UUID

@PersistenceTest
class PriceRepositoryFacadeTest : PersistenceBaseTest() {

    @Autowired
    private lateinit var priceRepositoryFacade: PriceRepositoryFacade

    @Autowired
    @Suppress("unused")
    private lateinit var connectionHolder: ConnectionHolder

    private val stationId1 = UUID.fromString("5cefce28-9c3a-4882-ae0a-0e7a0855ed01").stationId()
    private val updateId = 1565558887L
    private val updateTimestamp = Instant.ofEpochMilli(1565558887001)
    private val priceDiesel = 1.34
    private val priceE5 = 1.35
    private val priceE10 = 1.36

    @Test
    @DataSet("datasets/prices_initial.yml")
    @ExpectedDataSet("datasets/price_insert.yml")
    fun `should insert successfully`() {

        val price = ValidPriceInsert(
            stationId1,
            updateId,
            updateTimestamp,
            priceDiesel,
            priceE5,
            priceE10
        )

        val result = priceRepositoryFacade.insertPrice(price).unsafeRunSync()
        assertThat(result).isEqualTo(
            Price(
                stationId1,
                updateId,
                updateTimestamp,
                priceDiesel,
                priceE5,
                priceE10
            )
        )
    }

    @Test
    @DataSet("datasets/price_insert.yml")
    @ExpectedDataSet("datasets/price_insert.yml")
    fun `should throw on duplicate insert`() {

        val price = ValidPriceInsert(
            stationId1,
            updateId,
            updateTimestamp,
            priceDiesel,
            priceE5,
            priceE10
        )

        assertThrows<DataIntegrityViolationException> { priceRepositoryFacade.insertPrice(price).unsafeRunSync() }
    }
}