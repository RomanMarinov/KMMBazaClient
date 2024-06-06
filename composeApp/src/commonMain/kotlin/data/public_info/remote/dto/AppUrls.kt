package data.public_info.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class AppUrls(
    val appStoreUrl: String,
    val googlePlay: String,
    val googlePlayUrl: String,
    val ios: Int
)