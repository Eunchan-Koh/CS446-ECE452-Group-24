package com.backend.mealmatesapi.services

import jakarta.annotation.PostConstruct
import org.apache.juli.logging.Log
import org.apache.logging.log4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Import
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
import org.postgresql.util.PGobject
import java.sql.*

@Service

class DatabaseService {
  var db: Connection? = null
  @PostConstruct
  private fun main() {
    try {
      val jdbcUrl = "jdbc:postgresql://neondb_owner:h2BQKdmkGNW9@ep-shrill-heart-a5jj2nsr.us-east-2.aws.neon.tech/neondb?sslmode=require"
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
  //create function to take query result and prepare data in an array where each array is a row of the result.
  fun prepareData(data: ResultSet): ArrayList<ArrayList<Any>> {
    val result = ArrayList<ArrayList<Any>>()
    val rsmd:ResultSetMetaData = data.metaData

    while (data.next()) {
      val row = ArrayList<Any>()
      for (i in 1..rsmd.columnCount) {
        if (rsmd.getColumnTypeName(i) == "date") {
          row.add(data.getDate(i))
        } else if (rsmd.getColumnTypeName(i) == "jsonb") {
          row.add(data.getObject(i) as PGobject)
        } else if (rsmd.getColumnTypeName(i) == "serial" || rsmd.getColumnTypeName(i) == "int4") {
          row.add(data.getInt(i))
        } else if (rsmd.getColumnTypeName(i) == "_time") {
          row.add(data.getArray(i).array as Array<Time>)
        } else if (rsmd.getColumnTypeName(i) == "bool") {
            row.add(data.getBoolean(i))
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
