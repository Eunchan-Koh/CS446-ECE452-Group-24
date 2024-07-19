package com.example.mealmates.models

import com.google.android.gms.maps.model.LatLng
import kotlinx.serialization.Serializable

@Serializable
data class Group(
    val gid: Int,
    val name: String,
    val uids: List<String>,
    val preferences: List<String> = emptyList(),
    val restrictions: List<String> = emptyList(),
    val image: ByteArray = ByteArray(0),
    @Serializable(with = LatLngSerializer::class) var location: LatLng = LatLng(0.0, 0.0)
) {
    override fun toString(): String {
        return "Group(id=$gid, uids='$uids', name='$name', preferences='$preferences', restrictions='$restrictions')"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Group

        if (gid != other.gid) return false
        if (name != other.name) return false
        if (uids != other.uids) return false
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
