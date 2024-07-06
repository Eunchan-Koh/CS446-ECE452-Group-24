package com.example.mealmates.examples

import com.example.mealmates.constants.RestaurantTypes
import com.example.mealmates.models.SearchNearbyRequest
import com.google.android.gms.maps.model.LatLng

val PLACES_API_NEARBY_SEARCH_BODY_EXAMPLES =
    listOf(
        SearchNearbyRequest(
            includedTypes = listOf(RestaurantTypes.RESTAURANT, RestaurantTypes.CAFE, RestaurantTypes.HAMBURGER),
            excludedTypes = listOf(RestaurantTypes.VEGETARIAN),
            maxResultCount = 10,
            center = LatLng(40.7580, -73.9855),
            radius = 1000.0
        ).request,
        SearchNearbyRequest(
            includedTypes = listOf(RestaurantTypes.FAST_FOOD, RestaurantTypes.SUSHI),
            excludedTypes = listOf(RestaurantTypes.PIZZA),
            maxResultCount = 15,
            center = LatLng(43.4723, -80.5448),
            radius = 1000.0
        ).request
    )