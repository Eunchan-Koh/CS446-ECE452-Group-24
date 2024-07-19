package com.backend.mealmatesapi.services

import jakarta.annotation.PostConstruct
import kotlinx.serialization.json.JsonObject
import org.postgresql.geometric.PGpoint
import org.springframework.stereotype.Service
import java.awt.geom.Point2D
import java.sql.*

@Service

class DatabaseService {
    var db: Connection? = null

    @PostConstruct
    private fun main() {
        try {
            val jdbcUrl =
                "jdbc:postgresql://ep-shrill-heart-a5jj2nsr.us-east-2.aws.neon.tech/neondb?user=admin&password=bHMaV7rUIXk3&sslmode=require"
            val connection = DriverManager.getConnection(jdbcUrl, "postgres", "postgres")
            if (!connection.isValid(0)) {
                throw Error("Failed to connect to database with jdbc")
            } else {
                db = connection
            }
        } catch (e: Error) {
            System.err.println(e)
        }
    }

    // Create function to take query result and prepare data in an array where each array is a row of the result.
    fun prepareData(data: ResultSet): ArrayList<ArrayList<Any>> {
        val result = ArrayList<ArrayList<Any>>()
        val rsmd: ResultSetMetaData = data.metaData

        while (data.next()) {
            val row = ArrayList<Any>()
            for (i in 1..rsmd.columnCount) {
                println(rsmd.getColumnTypeName(i))
                if (rsmd.getColumnTypeName(i) == "date" && data.getArray(i) != null) {
                    row.add(data.getDate(i))
                } else if (rsmd.getColumnTypeName(i) == "jsonb" && data.getArray(i) != null) {
                    row.add(data.getObject(i) as JsonObject)
                } else if (rsmd.getColumnTypeName(i) == "serial" || rsmd.getColumnTypeName(i) == "int4" && data.getArray(
                        i
                    ) != null
                ) {
                    row.add(data.getInt(i))
                } else if (rsmd.getColumnTypeName(i) == "_text" && data.getArray(i) != null) {
                    row.add(data.getArray(i).array as Array<String>)
                } else if (rsmd.getColumnTypeName(i) == "bool" && data.getArray(i) != null) {
                    row.add(data.getBoolean(i))
                } else if (rsmd.getColumnTypeName(i) == "point" && data.getObject(i) != null) {
                    val pgPoint = data.getObject(i, PGpoint::class.java)
                    row.add(Point2D.Double(pgPoint.x, pgPoint.y))
                } else if (rsmd.getColumnTypeName(i) == "bytea" && data.getBytes(i) != null) {
                    row.add(data.getBytes(i) as ByteArray)
                } else {
                    row.add(data.getString(i))
                }
            }
            result.add(row)
        }
        return result
    }

    fun query(queryString: String): ArrayList<ArrayList<Any>>? {
        try {
            if (db == null) {
                throw Error("Database connection is null")
            }

            val query = db!!.prepareStatement(queryString)

            val result = prepareData(query.executeQuery())

            return result

        } catch (e: Error) {
            System.err.println(e)
            return null
        }
    }
}
