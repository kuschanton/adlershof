package com.akushch.adlershof.persistence.config

import com.github.database.rider.core.api.connection.ConnectionHolder
import java.sql.Connection
import javax.sql.DataSource

class DatasourceConnectionHolder(private val dataSource: DataSource) : ConnectionHolder {
    private val lazyConnection by lazy { dataSource.connection }
    override fun getConnection(): Connection = lazyConnection
}