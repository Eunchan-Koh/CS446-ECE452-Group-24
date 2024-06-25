package com.backend.mealmatesapi.models

import kotlinx.serialization.Serializable
@Serializable
class Restaurant(var pid: String = "", var _name: String = "", var _email: String = "") {
    var name: String = _name
        set(value) {
            field = value
        }

    var email: String = _email
        set(value) {
            field = value
        }

    var successfulChange = false
        set(value) {
            field = value
        }
    var errorMessage = ""
        set(value) {
            field = value
        }
    var isError = false
        set(value) {
            field = value
        }

    var submitEnabled = false
        set(value) {
            field = value
        }

    var changesSubmitted = false
        set(value) {
            field = value
        }

    override fun toString(): String {
        return "Restaurant(pid=$pid, name='$name', email='$email')"
    }
}