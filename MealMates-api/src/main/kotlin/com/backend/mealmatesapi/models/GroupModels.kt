package com.backend.mealmatesapi.models

import kotlinx.serialization.Serializable
import kotlinx.serialization.*
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.awt.geom.Point2D

@Serializable
data class Group(
    val gid: Int,
    val name: String,
    val uids: List<String>,
    val preferences: List<String> = emptyList(),
    val restrictions: List<String> = emptyList(),
    val image: ByteArray = ByteArray(0),
    @Serializable(with = Point2DSerializer::class)
    val location: Point2D.Double = Point2D.Double(),
) {
    @OptIn(ExperimentalSerializationApi::class)
    @Serializer(forClass = Point2D.Double::class)
    class Point2DSerializer {

        override fun serialize(encoder: Encoder, value: Point2D.Double) {
            encoder.encodeString("${value.x},${value.y}")
        }

        override fun deserialize(decoder: Decoder): Point2D.Double {
            val (x, y) = decoder.decodeString().split(",").map { it.toDouble() }
            return Point2D.Double(x, y)
        }
    }

    override fun toString(): String {
        return "Group(id=$gid, uids='$uids', name='$name', preferences='$preferences', restrictions='$restrictions')"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Group

        if (gid != other.gid) return false
        if (name != other.name) return false
        if(uids != other.uids) return false
        if (preferences != other.preferences) return false
        if (restrictions != other.restrictions) return false
        if (!image.contentEquals(other.image)) return false
        if (location != other.location) return false

        return true
    }

    override fun hashCode(): Int {
        var result = gid.hashCode()
        result = 31 * result + uids.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + preferences.hashCode()
        result = 31 * result + restrictions.hashCode()
        result = 31 * result + image.contentHashCode()
        result = 31 * result + location.hashCode()
        return result
    }
}
