package com.backend.mealmatesapi.models

import kotlinx.serialization.Serializable
@Serializable
data class Group(
    val id: String,
    val auid: String,
    val name: String,
    val uids: List<String>,
    val preferences: List<String>,
    val restrictions: List<String>,
//    val location:
) {
    override fun toString(): String {
        return "Group(id=$id, auid='$auid', name='$name', uids='$uids', preferences='$preferences', restrictions='$restrictions')"
    }
}
