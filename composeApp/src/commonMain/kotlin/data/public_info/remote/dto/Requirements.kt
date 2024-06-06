package data.public_info.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class Requirements(
    val apiVersion: Int,
    val appVersion: Int
)