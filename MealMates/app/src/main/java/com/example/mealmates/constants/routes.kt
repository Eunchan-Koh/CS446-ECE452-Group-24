package com.example.mealmates.constants

object Routes {
    val HOME = "home"
    val SURVEY = "survey"
    val LOCATION = "location"
    val RESTAURANT_PROMPTS = "restaurant_prompts"
    val MATCHED_RESTAURANTS = "matched_restaurants"
    val GROUP = "group"
    val CREATE_NEW_GROUP = "create_new_group"
    val GROUP_MEMBERS = "group_members"
    val PLACES_API_TEST = "places_test"
    val PROFILE = "profile"
    val GROUP_INFO = "group_info"
    val GROUP_SETTINGS = "group_settings"
    val MATCH_LIST = "match_list"

    val RESTAURANT_PROMPTS_WITH_ARGS =
        RESTAURANT_PROMPTS + "?" + "${NavArguments.GROUP_INFO.GROUP_ID}={groupId}"

    val GROUP_INFO_WITH_ARGS =
        GROUP_INFO +
            "?" +
            "${NavArguments.GROUP_INFO.GROUP_ID}={groupId}&" +
            "${NavArguments.GROUP_INFO.GROUP_NAME}={groupName}&" +
            "${NavArguments.GROUP_INFO.USERS}={users}&" +
            "${NavArguments.GROUP_INFO.PREFERENCES}={preferences}&" +
            "${NavArguments.GROUP_INFO.RESTRICTIONS}={restrictions}&" +
            "${NavArguments.GROUP_INFO.IMAGE}={image}&" +
            "${NavArguments.GROUP_INFO.LOCATION}={location}"

    val GROUP_SETTINGS_WITH_ARGS =
        GROUP_SETTINGS +
            "?" +
            "${NavArguments.GROUP_INFO.GROUP_ID}={groupId}&" +
            "${NavArguments.GROUP_INFO.GROUP_NAME}={groupName}&" +
            "${NavArguments.GROUP_INFO.USERS}={users}&" +
            "${NavArguments.GROUP_INFO.PREFERENCES}={preferences}&" +
            "${NavArguments.GROUP_INFO.RESTRICTIONS}={restrictions}&" +
            "${NavArguments.GROUP_INFO.IMAGE}={image}&" +
            "${NavArguments.GROUP_INFO.LOCATION}={location}"

    val MATCH_LIST_WITH_ARGS = MATCH_LIST + "?" +
            "${NavArguments.GROUP_INFO.GROUP_ID}={groupId}"

    val MATCHED_RESTAURANTS_WITH_ARGS = MATCHED_RESTAURANTS + "?" +
            "${NavArguments.RESTAURANT_INFO.RESTAURANT_ID}={restaurantId}"
}

object NavArguments {
    object GROUP_INFO {
        val GROUP_ID = "groupId"
        val GROUP_NAME = "groupName"
        val USERS = "users"
        val PREFERENCES = "preferences"
        val RESTRICTIONS = "restrictions"
        val IMAGE = "image"
        val LOCATION = "location"
    }

    object RESTAURANT_INFO {
        val RESTAURANT_ID = "restaurantId"
    }
}
