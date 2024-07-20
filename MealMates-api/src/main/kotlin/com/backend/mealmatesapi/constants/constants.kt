package com.backend.mealmatesapi.constants

object FieldMasks {
    // depending on our needs we may have to add more to this
    val SEARCH_NEARBY_DEFAULT = "places.id,places.displayName,places.formattedAddress,places.location,places.photos,places.types,places.rating"
    val PLACE_DETAILS_DEFAULT = "id,displayName,types,photos,adrFormatAddress,currentOpeningHours,priceLevel,rating,websiteUri"
}