package com.backend.mealmatesapi.controllers

import com.backend.mealmatesapi.models.Restaurants
import com.backend.mealmatesapi.services.DatabaseService
import kotlinx.serialization.json.JsonObject
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.ComponentScan
import org.springframework.web.bind.annotation.*

@RestController
@ComponentScan("com.backend.mealmatesapi.services")
@ComponentScan("com.backend.mealmatesapi.models")
@RequestMapping("/restaurants")
class RestaurantController {
    @Autowired
    lateinit var databaseService: DatabaseService

    @GetMapping("")
    @ResponseBody
    fun getRestaurants(@RequestParam("gid") gid: String): List<Restaurants> {
        val result: List<List<Any>>? = databaseService.query("SELECT * FROM Restaurants r where r.gid = '$gid';")
        println(result)
        println(gid)
        if (result.isNullOrEmpty()) {
            return listOf(Restaurants(-1, -1))
        } else {
            return result.map {
                Restaurants(
                    it[0] as Int,
                    it[1] as Int,
                    it[2] as JsonObject,
                    (it[3] as Array<String>).toList()
                )
            }
        }
    }

    @PostMapping("")
    @ResponseBody
    fun createRestaurants(@RequestBody restaurant: Restaurants): String {
        val query =
            "INSERT INTO Restaurants (gid, matched, suggested) VALUES ('${restaurant.gid}', '${restaurant.matched}', ARRAY[${
                restaurant.suggested.joinToString(",") { "'$it'" }
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
            queryStr += "suggested = ARRAY[${restaurant.suggested.joinToString(",") { "'$it'" }}]::text[], "
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

    @GetMapping("/completed")
    @ResponseBody
    fun getCompletedRestaurants(@RequestParam gid: String): List<Restaurants> {
        val result: List<List<Any>>? =
            databaseService.query("SELECT r.* FROM restaurants r JOIN groups g ON r.gid = g.gid WHERE r.gid = $gid AND jsonb_array_length(r.matched -> 'completed') >= array_length(g.uids, 1);")
        if (result.isNullOrEmpty()) {
            return listOf()
        } else {
            return result.map {
                Restaurants(
                    it[0] as Int,
                    it[1] as Int,
                    it[2] as JsonObject,
                    (it[3] as Array<String>).toList()
                )
            }
        }
    }

    @GetMapping("/single")
    @ResponseBody
    fun getSingleRestaurant(@RequestParam rid: String): Restaurants {
        val result: List<List<Any>>? = databaseService.query("SELECT * FROM Restaurants r where r.rid = '$rid';")
        println(result)
        if (result.isNullOrEmpty()) {
            return Restaurants(-1, -1)
        } else {
            return Restaurants(
                result[0][0] as Int,
                result[0][1] as Int,
                result[0][2] as JsonObject,
                (result[0][3] as Array<String>).toList()
            )
        }
    }
}
