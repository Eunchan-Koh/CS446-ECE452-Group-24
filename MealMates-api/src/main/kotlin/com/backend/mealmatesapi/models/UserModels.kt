package com.backend.mealmatesapi.models

import kotlinx.serialization.*
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.awt.Point
import java.sql.Time
import java.sql.Date

@Serializable
data class User(
    val id: String,
    val email: String,
    val name: String,
    val preferences: List<String> = emptyList(),
    val restrictions: List<String> = emptyList(),
    val image: ByteArray = ByteArray(0),
    @Serializable(with = PointSerializer::class)
    val location: Point = Point(0, 0),
) {
    @Serializer(forClass = Point::class)
    class PointSerializer {

        override fun serialize(encoder: Encoder, value: Point) {
            encoder.encodeString("${value.x},${value.y}")
        }

        override fun deserialize(decoder: Decoder): Point {
            val (x, y) = decoder.decodeString().split(",").map { it.toInt() }
            return Point(x, y)
        }
    }

    override fun toString(): String {
        return "User(id=$id, email='$email', name='$name', preferences='$preferences', restrictions='$restrictions')"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as User

        if (id != other.id) return false
        if (email != other.email) return false
        if (name != other.name) return false
        if (preferences != other.preferences) return false
        if (restrictions != other.restrictions) return false
        if (!image.contentEquals(other.image)) return false
        if (location != other.location) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + email.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + preferences.hashCode()
        result = 31 * result + restrictions.hashCode()
        result = 31 * result + image.contentHashCode()
        result = 31 * result + location.hashCode()
        return result
    }
}