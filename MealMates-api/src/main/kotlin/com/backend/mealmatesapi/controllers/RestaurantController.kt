package com.backend.mealmatesapi.controllers

import com.backend.mealmatesapi.models.Restaurant
import com.backend.mealmatesapi.services.DatabaseService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.ComponentScan
import org.springframework.web.bind.annotation.*
import java.sql.Date
import java.sql.Time

@RestController
@ComponentScan("com.backend.mealmatesapi.services")
@ComponentScan("com.backend.mealmatesapi.models")
@RequestMapping("/restaurant")
class RestaurantController {
    @Autowired
    lateinit var databaseService: DatabaseService
    
}
