package com.akushch.adlershof.persistence.container

import org.testcontainers.containers.PostgreSQLContainer

class PSQLContainer : PostgreSQLContainer<PSQLContainer>("postgres:10.5")