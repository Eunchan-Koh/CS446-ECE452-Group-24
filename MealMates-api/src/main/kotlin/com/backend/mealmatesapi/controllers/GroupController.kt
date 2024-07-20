package com.backend.mealmatesapi.controllers

import com.backend.mealmatesapi.services.DatabaseService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.ComponentScan
import com.backend.mealmatesapi.models.Group
import org.springframework.web.bind.annotation.*
import java.awt.geom.Point2D
import java.util.*

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
        if (result.isNullOrEmpty()) {
            return Group(-1, "Group not found", listOf(), listOf(), listOf(), ByteArray(0), Point2D.Double())
        } else if (result[0][0] is Int && result[0][1] is String && result[0][2] is Array<*> && result[0][3] is Array<*> &&
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

            return Group(
                result[0][0] as Int, result[0][1] as String, (result[0][2] as Array<String>).toList(),
                (result[0][3] as Array<String>).toList(), (result[0][4] as Array<String>).toList(), image, location
            )
        } else {
            return Group(-1, "Group not found", listOf(), listOf(), listOf(), ByteArray(0), Point2D.Double())
        }
    }

    @PostMapping("")
    @ResponseBody
    fun createGroup(@RequestBody group: Group): String {
        val imageForQuery = if (group.image.isNotEmpty()) {
            "'${Base64.getEncoder().encodeToString(group.image)}'"
        } else {
            "NULL"
        }
        val locationForQuery = if (group.location != Point2D.Double()) {
            "point(${group.location.x}, ${group.location.y})"
        } else {
            "NULL"
        }
        val query =
            "INSERT INTO Groups (name, uids, preferences, restrictions, image, location) VALUES ('${group.name}', ARRAY[${
                group.uids.joinToString(",") { "'$it'" }
            }]::text[], ARRAY[${group.preferences.joinToString(",") { "'$it'" }}]::text[], ARRAY[${
                group.restrictions.joinToString(
                    ","
                ) { "'$it'" }
            }]::text[], ${imageForQuery}, ${locationForQuery}) RETURNING gid;"

        databaseService.query(query)
        return "Inserted group with id ${group.gid}, name ${group.name}, uids ${group.uids}, preferences ${group.preferences}, restrictions ${group.restrictions}"
    }

    @PutMapping("")
    @ResponseBody
    fun updateGroup(@RequestBody group: Group): String {
        val imageForQuery = if (group.image.isNotEmpty()) {
            "'${Base64.getEncoder().encodeToString(group.image)}'"
        } else {
            "NULL"
        }
        val locationForQuery = if (group.location != Point2D.Double()) {
            "point(${group.location.x}, ${group.location.y})"
        } else {
            "NULL"
        }
        var queryStr = "UPDATE Groups SET "
        if (group.name.isNotEmpty()) {
            queryStr += "name = '${group.name}', "
        }
        if (group.uids.isNotEmpty()) {
            queryStr += "uids = ARRAY[${group.uids.joinToString(",") { "'$it'" }}], "
        }
        if (group.preferences.isNotEmpty()) {
            queryStr += "preferences = ARRAY[${group.preferences.joinToString(",") { "'$it'" }}], "
        }
        if (group.restrictions.isNotEmpty()) {
            queryStr += "restrictions = ARRAY[${group.restrictions.joinToString(",") { "'$it'" }}], "
        }
        if (group.image.isNotEmpty()) {
            queryStr += "image = ${imageForQuery}, "
        }
        if (group.location != Point2D.Double()) {
            queryStr += "location = ${locationForQuery}, "
        }
        queryStr = queryStr.dropLast(2)
        queryStr += " WHERE gid = ${group.gid};"
        databaseService.query(queryStr)
        return "Updated group with id ${group.gid}, name ${group.name}, uids ${group.uids}, preferences ${group.preferences}, restrictions ${group.restrictions}, image ${group.image}, location ${group.location} RETURNING gid"
    }
}