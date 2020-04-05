package com.akushch.adlershof.persistence.container

import org.testcontainers.containers.PostgreSQLContainer

class PSQLContainer : PostgreSQLContainer<PSQLContainer>("postgres:10.5") {
    val port : Int
        get() = getMappedPort(POSTGRESQL_PORT)
}