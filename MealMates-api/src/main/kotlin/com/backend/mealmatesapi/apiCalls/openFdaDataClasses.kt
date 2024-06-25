package com.backend.mealmatesapi.apiCalls

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class Meta(
    val disclaimer: String,
    val terms: String,
    val license: String,
    @SerialName("last_updated")
    val lastUpdated: String,
    val results: Results
)

@Serializable
data class Results(
    val skip: Int,
    val limit: Int,
    val total: Int
)
