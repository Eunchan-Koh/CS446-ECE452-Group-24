package com.backend.mealmatesapi.models

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

@Serializable
data class Restaurants(
    val rid: Int,
    val gid: Int,
    val matched: JsonObject = JsonObject(emptyMap()),
    val suggested: List<String> = emptyList(),
) {

    override fun toString(): String {
        return "Restaurants(rid=$rid, gid=$gid, matched='$matched', suggested='$suggested')"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Restaurants

        if (rid != other.gid) return false
        if (gid != other.gid) return false
        if (matched != other.matched) return false
        if (suggested != other.suggested) return false


        return true
    }

    override fun hashCode(): Int {
        var result = rid.hashCode()
        result = 31 * result + gid.hashCode()
        result = 31 * result + matched.hashCode()
        result = 31 * result + suggested.hashCode()
        return result
    }
}
