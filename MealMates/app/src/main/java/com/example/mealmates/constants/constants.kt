package com.example.mealmates.constants

import com.example.mealmates.models.User

object GlobalObjects {
    var user: User = User()
}

enum class GROUP_STATE {
    MATCH,
    VOTE,
    FINAL
}
