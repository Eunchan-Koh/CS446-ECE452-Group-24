package com.example.mealmates.models

import com.google.android.gms.maps.model.LatLng

data class SearchCircle(
    val center: LatLng,
    val radius: Double
)

data class LocationRestriction(
    val circle: SearchCircle
)

data class SearchNearbyBody(
    val includedTypes: List<String>,
    val excludedTypes: List<String>,
    val maxResultCount: Int,
    val locationRestriction: LocationRestriction
)
class SearchNearbyRequest(
    includedTypes: List<String>,
    excludedTypes: List<String>,
    maxResultCount: Int,
    center: LatLng,
    radius: Double
) {
    var locationRestriction = LocationRestriction(SearchCircle(center, radius))
    var request = SearchNearbyBody(includedTypes, excludedTypes, maxResultCount, locationRestriction)
}
