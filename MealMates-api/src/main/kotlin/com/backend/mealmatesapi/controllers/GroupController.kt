package com.backend.mealmatesapi.controllers

import com.backend.mealmatesapi.services.DatabaseService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.ComponentScan
import com.backend.mealmatesapi.models.Group
import org.springframework.web.bind.annotation.*

@RestController
@ComponentScan("com.backend.mealmatesapi.services")
@ComponentScan("com.backend.mealmatesapi.models")
@RequestMapping("/group")
class GroupController {
    @Autowired
    lateinit var databaseService: DatabaseService

}