package com.akushch.adlershof.persistence.config

import org.dbunit.dataset.datatype.AbstractDataType
import org.dbunit.dataset.datatype.DataType
import org.dbunit.ext.postgresql.PostgresqlDataTypeFactory
import org.postgresql.geometric.PGpoint
import org.postgresql.util.PGobject
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Types

class PostgresqlDataTypeFactory : PostgresqlDataTypeFactory() {
    override fun createDataType(sqlType: Int, sqlTypeName: String?): DataType {
        return when (sqlTypeName) {
            "point" -> PointDataType
            else -> super.createDataType(sqlType, sqlTypeName)
        }
    }

    object PointDataType : AbstractDataType("point", Types.OTHER, PGpoint::class.java, false) {
        override fun typeCast(obj: Any?): Any? = obj?.toString()

        override fun getSqlValue(column: Int, resultSet: ResultSet): Any {
            return resultSet.getString(column)
        }

        override fun setSqlValue(obj: Any?, column: Int, statement: PreparedStatement) {
            statement.setObject(column, PGobject().apply {
                type = "point"
                value = obj?.toString()
            })
        }
    }
}