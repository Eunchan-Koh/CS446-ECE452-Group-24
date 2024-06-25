package com.gradle.models

import com.auth0.android.jwt.JWT


class User(val idToken: String? = null, var type: String = "", var name: String = "") {

    private val TAG = "User"

    var id = ""
    var email = ""

    init {
        if (idToken != null) {
            try {
                val jwt = JWT(idToken)
                var idString = jwt.subject ?: "1"

                if (idString.startsWith("auth0|")) {
                    idString = idString.slice(6 until idString.length)
                }
                id = idString
                name = jwt.getClaim("name").asString() ?: ""
                email = jwt.getClaim("email").asString() ?: ""
            } catch (error: com.auth0.android.jwt.DecodeException) {
                println(TAG + "Error occurred trying to decode JWT: $error ")
            }
        } else {
            println(TAG + "User is logged out - instantiating empty User object.")
        }
    }

}
