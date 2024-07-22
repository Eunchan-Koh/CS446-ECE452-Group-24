package com.backend.mealmatesapi.constants

object FieldMasks {
    // depending on our needs we may have to add more to this
    val SEARCH_NEARBY_DEFAULT = "places.id,places.displayName,places.shortFormattedAddress,places.location,places.photos,places.types,places.rating"
    val PLACE_DETAILS_DEFAULT = "id,displayName,types,photos,adrFormatAddress,location,currentOpeningHours,priceLevel,rating,websiteUri"
}