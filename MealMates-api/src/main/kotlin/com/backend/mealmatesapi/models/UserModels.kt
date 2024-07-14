package com.backend.mealmatesapi.models

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.sql.Time
import java.sql.Date

@Serializable
data class User(
    val id: String,
    val email: String,
    val name: String,
    val preferences: List<String>,
    val restrictions: List<String>,
//    val location:
) {
    override fun toString(): String {
        return "User(id=$id, email='$email', name='$name', preferences='$preferences', restrictions='$restrictions')"
    }
}