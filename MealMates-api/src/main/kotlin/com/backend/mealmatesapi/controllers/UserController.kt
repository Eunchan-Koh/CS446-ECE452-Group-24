package com.backend.mealmatesapi.controllers

import org.springframework.context.annotation.ComponentScan
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController
import com.backend.mealmatesapi.models.User
import com.backend.mealmatesapi.services.DatabaseService
import com.google.gson.JsonObject
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping

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
        if(result.isNullOrEmpty()) {
            return User("1", "User not found", "User not found", listOf(), listOf())
        } else if(result[0][0] is String && result[0][1] is String && result[0][2] is String) {
            return User(result[0][0] as String, result[0][1] as String, result[0][2] as String,
                        result[0][3] as List<String>, result[0][4] as List<String>)
        } else {
            return User("1", "User not found", "User not found", listOf(), listOf())
        }
    }

    @PostMapping("")
    @ResponseBody
    fun createUser(@RequestParam("id") id: String, @RequestParam("email") email: String,
                   @RequestParam("name") name: String, @RequestParam("preferences") preferences: List<String>,
                   @RequestParam("restrictions") restrictions: List<String>): String {
        val result: List<List<Any>>? = databaseService.query("INSERT INTO Users (uid, email, name, preferences, restrictions) VALUES ('$id', '$email', '$name', ARRAY[${preferences.joinToString(",")}], ARRAY[${restrictions.joinToString(",")}]);")
        return "Inserted user with id $id, email $email, name $name, preferences $preferences, restrictions $restrictions"
    }

    @PutMapping("")
    @ResponseBody
    fun updateUser(@RequestParam("id") id: String, @RequestParam("email") email: String,
                   @RequestParam("name") name: String, @RequestParam("preferences") preferences: List<String>,
                   @RequestParam("restrictions") restrictions: List<String>): String {
        var queryStr = "UPDATE User SET "
        if(email.isNotEmpty()) {
            queryStr += "email = '$email', "
        }
        if(name.isNotEmpty()) {
            queryStr += "name = '$name', "
        }
        if(preferences.isNotEmpty()) {
            queryStr += "preferences = ARRAY[${preferences.joinToString(",")}], "
        }
        if(restrictions.isNotEmpty()) {
            queryStr += "restrictions = ARRAY[${restrictions.joinToString(",")}], "
        }
        queryStr = queryStr.dropLast(2)
        queryStr += " WHERE uid = '$id';"
        databaseService.query(queryStr)
        return "Updated user with id $id, email $email, name $name, preferences $preferences, restrictions $restrictions"
    }

}