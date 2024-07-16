package com.example.mealmates.models

import android.graphics.Point
import com.auth0.android.jwt.JWT
import kotlinx.serialization.Contextual
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

object PointSerializer : KSerializer<Point> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("Point", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Point) {
        encoder.encodeString("${value.x},${value.y}")
    }

    override fun deserialize(decoder: Decoder): Point {
        val (x, y) = decoder.decodeString().split(",").map { it.toInt() }
        return Point(x, y)
    }
}

@Serializable
class User(var id: String? = null,
           var name: String = "",
           var email: String = "",
           var preferences: List<String> = emptyList(),
           var restrictions: List<String> = emptyList(),
           var image: ByteArray = byteArrayOf(0),
           @Serializable(with = PointSerializer::class)
           var location: Point = Point(0, 0)) {


    private val TAG = "User"

    init {
        if (id != null) {
            try {
                val jwt = JWT(id!!)
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

    override fun toString(): String {
        return "User(id=$id, email='$email', name='$name', preferences='$preferences', restrictions='$restrictions', image=${image.contentToString()}, location=$location"
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
