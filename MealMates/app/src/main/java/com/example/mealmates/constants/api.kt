package com.example.mealmates.constants

object FieldMasks {
    // depending on our needs we may have to add more to this
    val DEFAULT =
        "places.id,places.displayName,places.shortFormattedAddress,places.location,places.photos,places.types,places.rating"
}

object RestaurantTypes {
    val AMERICAN = "american_restaurant"
    val BAKERY = "bakery"
    val BARBECUE = "barbecue_restaurant"
    val BRAZILIAN = "brazilian_restaurant"
    val BREAKFAST = "breakfast_restaurant"
    val BRUNCH = "brunch_restaurant"
    val CAFE = "cafe"
    val CHINESE = "chinese_restaurant"
    val COFFEE = "coffee_shop"
    val FAST_FOOD = "fast_food_restaurant"
    val FRENCH = "french_restaurant"
    val GREEK = "greek_restaurant"
    val HAMBURGER = "hamburger_restaurant"
    val ICE_CREAM = "ice_cream_shop"
    val INDIAN = "indian_restaurant"
    val INDONESIAN = "indonesian_restaurant"
    val ITALIAN = "italian_restaurant"
    val JAPANESE = "japanese_restaurant"
    val KOREAN = "korean_restaurant"
    val LEBANESE = "lebanese_restaurant"
    val MEAL_DELIVERY = "meal_delivery"
    val MEAL_TAKEAWAY = "meal_takeaway"
    val MEDITERRANEAN = "mediterranean_restaurant"
    val MEXICAN = "mexican_restaurant"
    val MIDDLE_EASTERN = "middle_eastern_restaurant"
    val PIZZA = "pizza_restaurant"
    val RAMEN = "ramen_restaurant"
    val RESTAURANT = "restaurant"
    val SANDWICH = "sandwich_shop"
    val SEAFOOD = "seafood_restaurant"
    val SPANISH = "spanish_restaurant"
    val STEAK_HOUSE = "steak_house"
    val SUSHI = "sushi_restaurant"
    val THAI = "thai_restaurant"
    val TURKISH = "turkish_restaurant"
    val VEGAN = "vegan_restaurant"
    val VEGETARIAN = "vegetarian_restaurant"
    val VIETNAMESE = "vietnamese_restaurant"
}

val RESTAURANT_TYPE_LABEL_LIST = mapOf<String, String>(
    "American" to RestaurantTypes.AMERICAN,
    "Bakery" to RestaurantTypes.BAKERY,
    "Barbecue" to RestaurantTypes.BARBECUE,
    "Brazilian" to RestaurantTypes.BRAZILIAN,
    "Breakfast" to RestaurantTypes.BREAKFAST,
    "Brunch" to RestaurantTypes.BRUNCH,
    "Cafe" to RestaurantTypes.CAFE,
    "Chinese" to RestaurantTypes.CHINESE,
    "Coffee" to RestaurantTypes.COFFEE,
    "Fast Food" to RestaurantTypes.FAST_FOOD,
    "French" to RestaurantTypes.FRENCH,
    "Greek" to RestaurantTypes.GREEK,
    "Hamburger" to RestaurantTypes.HAMBURGER,
    "Ice Cream" to RestaurantTypes.ICE_CREAM,
    "Indian" to RestaurantTypes.INDIAN,
    "Indonesian" to RestaurantTypes.INDONESIAN,
    "Italian" to RestaurantTypes.ITALIAN,
    "Japanese" to RestaurantTypes.JAPANESE,
    "Korean" to RestaurantTypes.KOREAN,
    "Lebanese" to RestaurantTypes.LEBANESE,
    "Meal Delivery" to RestaurantTypes.MEAL_DELIVERY,
    "Meal Takeaway" to RestaurantTypes.MEAL_TAKEAWAY,
    "Mediterranean" to RestaurantTypes.MEDITERRANEAN,
    "Mexican" to RestaurantTypes.MEXICAN,
    "Middle Eastern" to RestaurantTypes.MIDDLE_EASTERN,
    "Pizza" to RestaurantTypes.PIZZA,
    "Ramen" to RestaurantTypes.RAMEN,
    "Restaurant" to RestaurantTypes.RESTAURANT,
    "Sandwich" to RestaurantTypes.SANDWICH,
    "Seafood" to RestaurantTypes.SEAFOOD,
    "Spanish" to RestaurantTypes.SPANISH,
    "Steak House" to RestaurantTypes.STEAK_HOUSE,
    "Sushi" to RestaurantTypes.SUSHI,
    "Thai" to RestaurantTypes.THAI,
    "Turkish" to RestaurantTypes.TURKISH,
    "Vegan" to RestaurantTypes.VEGAN,
    "Vegetarian" to RestaurantTypes.VEGETARIAN,
    "Vietnamese" to RestaurantTypes.VIETNAMESE,
    )

// This is a reverse mapping of RestaurantTypes above with undesired labels removed
val RestaurantTypeToLabel =
    mapOf(
        "american_restaurant" to "AMERICAN",
        "bakery" to "BAKERY",
        "barbecue_restaurant" to "BARBECUE",
        "brazilian_restaurant" to "BRAZILIAN",
        "breakfast_restaurant" to "BREAKFAST",
        "brunch_restaurant" to "BRUNCH",
        "cafe" to "CAFE",
        "chinese_restaurant" to "CHINESE",
        "coffee_shop" to "COFFEE",
        "fast_food_restaurant" to "FAST FOOD",
        "french_restaurant" to "FRENCH",
        "greek_restaurant" to "GREEK",
        "hamburger_restaurant" to "HAMBURGER",
        "ice_cream_shop" to "ICE CREAM",
        "indian_restaurant" to "INDIAN",
        "indonesian_restaurant" to "INDONESIAN",
        "italian_restaurant" to "ITALIAN",
        "japanese_restaurant" to "JAPANESE",
        "korean_restaurant" to "KOREAN",
        "lebanese_restaurant" to "LEBANESE",
        "mediterranean_restaurant" to "MEDITERRANEAN",
        "mexican_restaurant" to "MEXICAN",
        "middle_eastern_restaurant" to "MIDDLE EASTERN",
        "pizza_restaurant" to "PIZZA",
        "ramen_restaurant" to "RAMEN",
        "sandwich_shop" to "SANDWICH",
        "seafood_restaurant" to "SEAFOOD",
        "spanish_restaurant" to "SPANISH",
        "steak_house" to "STEAK_HOUSE",
        "sushi_restaurant" to "SUSHI",
        "thai_restaurant" to "THAI",
        "turkish_restaurant" to "TURKISH",
        "vegan_restaurant" to "VEGAN",
        "vegetarian_restaurant" to "VEGETARIAN",
        "vietnamese_restaurant" to "VIETNAMESE")

    