package com.backend.mealmatesapi.models

import kotlinx.serialization.*
import java.awt.geom.Point2D

@Serializable
data class User(
    val id: String,
    val name: String,
    val email: String,
    val preferences: List<String> = emptyList(),
    val restrictions: List<String> = emptyList(),
    val image: ByteArray = ByteArray(0),
    @Serializable(with = Group.Point2DSerializer::class)
    val location: Point2D.Double = Point2D.Double(),
) {

    override fun toString(): String {
        return "User(id=$id, name='$name', email='$email', preferences='$preferences', restrictions='$restrictions')"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as User

        if (id != other.id) return false
        if (name != other.name) return false
        if (email != other.email) return false
        if (preferences != other.preferences) return false
        if (restrictions != other.restrictions) return false
        if (!image.contentEquals(other.image)) return false
        if (location != other.location) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + email.hashCode()
        result = 31 * result + preferences.hashCode()
        result = 31 * result + restrictions.hashCode()
        result = 31 * result + image.contentHashCode()
        result = 31 * result + location.hashCode()
        return result
    }
}