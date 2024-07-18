package com.backend.mealmatesapi.controllers

import com.backend.mealmatesapi.models.Restaurants
import com.backend.mealmatesapi.services.DatabaseService
import kotlinx.serialization.json.JsonObject
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.ComponentScan
import org.springframework.web.bind.annotation.*
import java.sql.Date
import java.sql.Time

@RestController
@ComponentScan("com.backend.mealmatesapi.services")
@ComponentScan("com.backend.mealmatesapi.models")
@RequestMapping("/restaurants")
class RestaurantController {
    @Autowired
    lateinit var databaseService: DatabaseService

    @GetMapping("")
    @ResponseBody
    fun getRestaurants(@RequestParam("gid") gid: String): Restaurants {
        val result: List<List<Any>>? = databaseService.query("SELECT * FROM Restaurants r where g.gid = '$gid';")
        if (result.isNullOrEmpty()) {
            return Restaurants(-1, -1)
        } else if (result[0][0] is Int && result[0][1] is Int && result[0][2] is JsonObject && result[0][3] is Array<*>) {
            return Restaurants(
                result[0][0] as Int,
                result[0][1] as Int,
                result[0][2] as JsonObject,
                (result[0][3] as Array<String>).toList()
            )
        } else {
            return Restaurants(-1, -1)
        }
    }

    @PostMapping("")
    @ResponseBody
    fun createRestaurants(@RequestBody restaurant: Restaurants): String {
        val query =
            "INSERT INTO Restaurants (rid, gid, matched, suggested) VALUES ('${restaurant.rid}', '${restaurant.gid}', '${restaurant.matched}', ARRAY[${
                restaurant.suggested.joinToString(",")
            }]::text[]) RETURNING rid;"
        databaseService.query(query)
        return "Inserted restaurant with rid ${restaurant.rid}, gid ${restaurant.gid}, matched ${restaurant.matched}, suggested ${restaurant.suggested}"
    }

    @PutMapping("")
    @ResponseBody
    fun updateRestaurants(@RequestBody restaurant: Restaurants): String {
        var queryStr = "UPDATE Restaurants SET "
        if (restaurant.matched.isNotEmpty()) {
            queryStr += "matched = '${restaurant.matched}', "
        }
        if (restaurant.suggested.isNotEmpty()) {
            queryStr += "suggested = ARRAY[${restaurant.suggested.joinToString(",")}]::text[], "
        }
        queryStr = queryStr.substring(0, queryStr.length - 2)
        queryStr += " WHERE rid = '${restaurant.rid}' AND gid = '${restaurant.gid}' RETURNING rid;"
        databaseService.query(queryStr)
        return "Updated restaurant with rid ${restaurant.rid}, gid ${restaurant.gid}, matched ${restaurant.matched}, suggested ${restaurant.suggested}"
    }

    @DeleteMapping("")
    @ResponseBody
    fun deleteRestaurants(@RequestParam gid: String): String {
        val result: List<List<Any>>? =
            databaseService.query("DELETE FROM Restaurants WHERE gid = '${gid}' RETURNING rid;")
        return "Deleted restaurants with rid ${result?.get(0)?.get(0)}"
    }

}
