package com.backend.mealmatesapi.controllers

import org.springframework.context.annotation.ComponentScan
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController
import com.backend.mealmatesapi.models.User
import com.google.gson.JsonObject

@RestController
@ComponentScan("com.backend.mealmatesapi.services")
@ComponentScan("com.backend.mealmatesapi.models")
@ComponentScan("com.backend.mealmatesapi.apiCalls")
@RequestMapping("/user")
class UserController {

}