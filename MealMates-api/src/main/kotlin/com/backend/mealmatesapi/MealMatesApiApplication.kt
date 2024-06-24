package com.backend.mealmatesapi

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan


@SpringBootApplication
@ComponentScan("com.backend.mealmatesapi.controllers")
class MealMatesApiApplication

fun main(args: Array<String>) {
	runApplication<MealMatesApiApplication>(*args)
}
