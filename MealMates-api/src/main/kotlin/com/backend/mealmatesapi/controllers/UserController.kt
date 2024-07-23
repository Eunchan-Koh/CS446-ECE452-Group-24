package com.backend.mealmatesapi.controllers

import org.springframework.context.annotation.ComponentScan
import com.backend.mealmatesapi.models.User
import com.backend.mealmatesapi.services.DatabaseService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import java.awt.Point
import com.backend.mealmatesapi.models.Group
import java.awt.geom.Point2D

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
        val result: List<List<Any>>? = databaseService.query("SELECT * FROM Users u where u.uid = '$id';")
        if (result.isNullOrEmpty()) {
            return User("", "User not found", "User not found", listOf(), listOf(), ByteArray(0), Point2D.Double())
        } else if (result[0][0] is String && result[0][1] is String && result[0][2] is String && result[0][3] is Array<*> &&
            result[0][4] is Array<*>
        ) {

            var image = ByteArray(0)
            var location = Point2D.Double()
            if (result[0][5] is String) {
                // TODO: May need to convert this differently
                image = result[0][5] as ByteArray
            }
            if (result[0][6] is Point2D.Double) {
                location = result[0][6] as Point2D.Double
            }

            return User(
                result[0][0] as String, result[0][1] as String, result[0][2] as String,
                (result[0][3] as Array<String>).toList(), (result[0][4] as Array<String>).toList(), image, location
            )
        } else {
            return User("", "User not found", "User not found", listOf(), listOf(), ByteArray(0), Point2D.Double())
        }
    }

    @GetMapping("/email")
    @ResponseBody
    fun getUserByEmail(@RequestParam("email") email: String): User {
        val result: List<List<Any>>? = databaseService.query("SELECT * FROM Users u where u.email = '$email';")
        if (result.isNullOrEmpty()) {
            return User("", "User not found", "User not found", listOf(), listOf(), ByteArray(0), Point2D.Double())
        } else if (result[0][0] is String && result[0][1] is String && result[0][2] is String && result[0][3] is Array<*> &&
            result[0][4] is Array<*>
        ) {

            var image = ByteArray(0)
            var location = Point2D.Double()
            if (result[0][5] is String) {
                // TODO: May need to convert this differently
                image = result[0][5] as ByteArray
            }
            if (result[0][6] is Point2D.Double) {
                location = result[0][6] as Point2D.Double
            }

            return User(
                result[0][0] as String, result[0][1] as String, result[0][2] as String,
                (result[0][3] as Array<String>).toList(), (result[0][4] as Array<String>).toList(), image, location
            )
        } else {
            return User("", "User not found", "User not found", listOf(), listOf(), ByteArray(0), Point2D.Double())
        }
    }

    @PostMapping("")
    @ResponseBody
    fun createUser(@RequestBody user: User): String {
        //TODO: Support for image and location. See GroupController.kt for example
        databaseService.query(
            "INSERT INTO Users (name, email, preferences, restrictions, image, location) VALUES ('${user.name}', '${user.email}', ARRAY[${
                user.preferences.joinToString(
                    ","
                ) { "'$it'" }
            }]::text[], ARRAY[${user.restrictions.joinToString(",") { "'$it'" }}]::text[], null, null) RETURNING uid;"
        )
        return "Inserted user with id ${user.id}, email ${user.email}, name ${user.name}, preferences ${user.preferences}, restrictions ${user.restrictions}"
    }

    @PutMapping("")
    @ResponseBody
    fun updateUser(@RequestBody user: User): String {
        //TODO: Support for image and location
        val locationForQuery = if (user.location != Point2D.Double()) {
            "point(${user.location.x}, ${user.location.y})"
        } else {
            "NULL"
        }
        var queryStr = "UPDATE Users SET "
        if (user.name.isNotEmpty()) {
            queryStr += "name = '${user.name}', "
        }
        if (user.email.isNotEmpty()) {
            queryStr += "email = '${user.email}', "
        }
        if (user.preferences.isNotEmpty()) {
            queryStr += "preferences = ARRAY[${user.preferences.joinToString(",") { "'$it'" }}], "
        }
        if (user.restrictions.isNotEmpty()) {
            queryStr += "restrictions = ARRAY[${user.restrictions.joinToString(",") { "'$it'" }}], "
        }
        if (user.location != Point(0, 0)) {
            queryStr += "location = ${locationForQuery}, "
        }
        queryStr = queryStr.dropLast(2)
        queryStr += " WHERE uid = '${user.id}';"
        databaseService.query(queryStr)
        return "Updated user with id ${user.id}, name ${user.name}, email ${user.email}, preferences ${user.preferences}, restrictions ${user.restrictions}, location ${user.location} RETURNING uid"
    }

    @GetMapping("/groups")
    @ResponseBody
    fun getUserGroups(@RequestParam id: String): List<Group> {
        println("this is id $id")
        val result: List<List<Any>>? =
            databaseService.query("SELECT g.* FROM Groups g WHERE g.uids @> ARRAY['$id']::text[];")
        if (result.isNullOrEmpty()) {
            return listOf()
        } else {
            val groups = mutableListOf<Group>()
            for (group in result) {
                var image = ByteArray(0)
                var location = Point2D.Double()
                if (group[5] is String) {
                    // TODO: May need to convert this differently
                    image = group[5] as ByteArray
                }
                if (group[6] is Point2D.Double) {
                    location = group[6] as Point2D.Double
                }
                groups.add(
                    Group(
                        group[0] as Int,
                        group[1] as String,
                        (group[2] as Array<String>).toList(),
                        (group[3] as Array<String>).toList(),
                        (group[4] as Array<String>).toList(),
                        image,
                        location
                    )
                )
            }
            return groups
        }
    }

}