package com.backend.mealmatesapi.controllers

import org.springframework.context.annotation.ComponentScan
import com.backend.mealmatesapi.models.User
import com.backend.mealmatesapi.services.DatabaseService
import com.google.gson.JsonObject
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import java.awt.Point

@RestController
@ComponentScan("com.backend.mealmatesapi.services")
@ComponentScan("com.backend.mealmatesapi.models")
@ComponentScan("com.backend.mealmatesapi.apiCalls")
@RequestMapping("/user")
class UserController {
    @Autowired
    lateinit var databaseService: DatabaseService

    @GetMapping("")
    @ResponseBody
    fun getUser(@RequestParam("id") id: String): User {
        println(id)
        val result: List<List<Any>>? = databaseService.query("SELECT * FROM Users u where u.uid = '$id';")
        if(result.isNullOrEmpty()) {
            return User("", "User not found", "User not found", listOf(), listOf(), ByteArray(0), Point(0, 0))
        } else if(result[0][0] is String && result[0][1] is String && result[0][2] is String && result[0][3] is List<*> &&
            result[0][4] is List<*> && result[0][5] is ByteArray && result[0][6] is Point) {

            return User(result[0][0] as String, result[0][1] as String, result[0][2] as String,
                        result[0][3] as List<String>, result[0][4] as List<String>, result[0][5] as ByteArray, result[0][6] as Point)
        } else {
            return User("", "User not found", "User not found", listOf(), listOf(), ByteArray(0), Point(0, 0))
        }
    }

    @PostMapping("")
    @ResponseBody
    fun createUser(@RequestBody user: User): String {
        //TODO: Support for image and location
        databaseService.query("INSERT INTO Users (uid, name, email, preferences, restrictions, image, location) VALUES ('${user.id}', '${user.name}', '${user.email}', ARRAY[${user.preferences.joinToString(",")}]::text[], ARRAY[${user.restrictions.joinToString(",")}]::text[], null, null) RETURNING uid;")
        return "Inserted user with id $user.id, email $user.email, name $user.name, preferences $user.preferences, restrictions $user.restrictions"
    }

    @PutMapping("")
    @ResponseBody
    fun updateUser(@RequestBody user: User): String {
        //TODO: Support for image and location
        var queryStr = "UPDATE User SET "
        if(user.email.isNotEmpty()) {
            queryStr += "email = '$user.email', "
        }
        if(user.name.isNotEmpty()) {
            queryStr += "name = '$user.name', "
        }
        if(user.preferences.isNotEmpty()) {
            queryStr += "preferences = ARRAY[${user.preferences.joinToString(",")}], "
        }
        if(user.restrictions.isNotEmpty()) {
            queryStr += "restrictions = ARRAY[${user.restrictions.joinToString(",")}], "
        }
        queryStr = queryStr.dropLast(2)
        queryStr += " WHERE uid = '$user.id';"
        databaseService.query(queryStr)
        return "Updated user with id $user.id, email $user.email, name $user.name, preferences $user.preferences, restrictions $user.restrictions RETURNING uid"
    }

}