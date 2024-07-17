package com.backend.mealmatesapi.controllers

import com.backend.mealmatesapi.services.DatabaseService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.ComponentScan
import com.backend.mealmatesapi.models.Group
import org.springframework.web.bind.annotation.*
import java.awt.Point

@RestController
@ComponentScan("com.backend.mealmatesapi.services")
@ComponentScan("com.backend.mealmatesapi.models")
@RequestMapping("/group")
class GroupController {
    @Autowired
    lateinit var databaseService: DatabaseService

    @GetMapping("")
    @ResponseBody
    fun getGroup(@RequestParam("id") id: String): Group {
        val result: List<List<Any>>? = databaseService.query("SELECT * FROM Groups g where g.gid = '$id';")
        if(result.isNullOrEmpty()) {
            return Group(-1, "Group not found", listOf(), listOf(), listOf(), ByteArray(0), Point(0,0))
        } else if(result[0][0] is String && result[0][1] is String && result[0][2] is String && result[0][3] is Array<*> &&
            result[0][4] is Array<*>) {
            var image = ByteArray(0)
            var location = Point(0,0)
            if(result[0][5] is ByteArray) {
                image = result[0][5] as ByteArray
            }
            if(result[0][6] is Point) {
                location = result[0][6] as Point
            }
            return Group(result[0][0] as Int, result[0][1] as String, (result[0][2] as Array<String>).toList(),
                (result[0][3] as Array<String>).toList(), (result[0][4] as Array<String>).toList(), image, location)
        } else {
            return Group(-1, "Group not found", listOf(), listOf(), listOf(), ByteArray(0), Point(0,0))
        }
    }

    @PostMapping("")
    @ResponseBody
    fun createGroup(@RequestBody group: Group): String {
        val imageForQuery = if(group.image.isNotEmpty()) {
            group.image
        } else {
            ByteArray(0)
        }
        val locationForQuery = if(group.location != Point(0,0)) {
            group.location
        } else {
            Point(0,0)
        }
        val query = "INSERT INTO Groups (gid, name, uids, preferences, restrictions, image, location) VALUES ('${group.gid}', '${group.name}', ARRAY[${group.uids.joinToString(",")}]::text[], ARRAY[${group.preferences.joinToString(",")}]::text[], ARRAY[${group.restrictions.joinToString(",")}]::text[], ${imageForQuery}, ${locationForQuery}) RETURNING gid;"
        databaseService.query(query)
        return "Inserted group with id ${group.gid}, name ${group.name}, uids ${group.uids}, preferences ${group.preferences}, restrictions ${group.restrictions}"
    }

    @PutMapping("")
    @ResponseBody
    fun updateGroup(@RequestBody group: Group): String {
        val imageForQuery = if(group.image.isNotEmpty()) {
            group.image
        } else {
            ByteArray(0)
        }
        val locationForQuery = if(group.location != Point(0,0)) {
            group.location
        } else {
            Point(0,0)
        }
        var queryStr = "UPDATE Groups SET "
        if(group.name.isNotEmpty()) {
            queryStr += "name = '${group.name}', "
        }
        if(group.uids.isNotEmpty()) {
            queryStr += "uids = ARRAY[${group.uids.joinToString(",")}], "
        }
        if(group.preferences.isNotEmpty()) {
            queryStr += "preferences = ARRAY[${group.preferences.joinToString(",")}], "
        }
        if(group.restrictions.isNotEmpty()) {
            queryStr += "restrictions = ARRAY[${group.restrictions.joinToString(",")}], "
        }
        if(group.image.isNotEmpty()) {
            queryStr += "image = ${imageForQuery}, "
        }
        if(group.location != Point(0,0)) {
            queryStr += "location = ${locationForQuery}, "
        }
        queryStr = queryStr.dropLast(2)
        queryStr += " WHERE gid = ${group.gid};"
        databaseService.query(queryStr)
        return "Updated group with id ${group.gid}, name ${group.name}, uids ${group.uids}, preferences ${group.preferences}, restrictions ${group.restrictions}, image ${group.image}, location ${group.location} RETURNING gid"
    }
}