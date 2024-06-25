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
    val id: Int,
    val username: String,
    val password: String,
    val email: String,
    val firstName: String,
    val lastName: String,
) {
    override fun toString(): String {
        return "User(id=$id, username='$username', password='$password', email='$email', firstName='$firstName', lastName='$lastName'"
    }
}