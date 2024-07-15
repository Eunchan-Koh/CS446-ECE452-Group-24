package com.example.mealmates.constants

import com.example.mealmates.ui.views.RestaurantInfo
import com.example.mealmates.models.User

object GlobalObjects {
    var user: User = User()
}

enum class GROUP_STATE {
    MATCH,
    VOTE,
    FINAL
}

val RESTAURANT_DATA =
    listOf(
        RestaurantInfo(
            name = "Gol's Lanzou Noodle",
            address = "150 University Ave W Unit 6B, Waterloo, ON N2L 3E4",
            website = "http://www.lanzhou.ca/",
            photos =
                listOf(
                    "https://lh5.googleusercontent.com/p/AF1QipMi2Wy7AWGgVy_CtZJiSBFr7V2iLm28BEjNcjMJ=w101-h126-n-k-no-nu",
                    "https://lh5.googleusercontent.com/p/AF1QipO-8GaKX8BhafiWtS9zf2cvXO-L91sB_4_2UYEI=w101-h168-n-k-no-nu",
                    "https://lh5.googleusercontent.com/p/AF1QipMuJQaNzk8Pby5URLp-ctUh1-0R7Q1cGeePCBlW=w141-h235-n-k-no-nu"),
            tags = listOf("Chinese")),
        RestaurantInfo(
            name = "Lazeez Shawarma",
            address = "170 University Ave W, Waterloo, ON N2L 3E9",
            website = "https://www.lazeezshawarma.com/",
            photos =
                listOf(
                    "https://lh5.googleusercontent.com/p/AF1QipO9BYoECm621og8GC0wxggq87gG2JOSm20FI5P5=w141-h118-n-k-no-nu",
                    "https://lh5.googleusercontent.com/p/AF1QipNm392wqqVvODh2iNPCEXvdPVSzq0MejvX5LBCt=w141-h235-n-k-no-nu",
                ),
            tags = listOf("Halal", "Gluten-Free")),
        RestaurantInfo(
            name = "Campus Pizza",
            address = "160 University Ave W #2, Waterloo, ON N2L 3E9",
            website = "http://www.campuspizza.ca/",
            photos =
                listOf(
                    "https://lh5.googleusercontent.com/p/AF1QipP7fDcPKBo7OfwtcEx4Qmr4uWurq7e_1D2Xvh-a=w141-h118-n-k-no-nu",
                    "https://lh5.googleusercontent.com/p/AF1QipOAGYi-SDADzNkfOCDKNa-5JXCUYvVzlAPV-y1O=w141-h176-n-k-no-nu",
                ),
            tags = listOf("Italian", "Vegan", "Halal", "Kosher")),
    )
