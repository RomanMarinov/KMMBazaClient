package data.public_info.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class PromoInfo(
    val description: String,
    val icon: String,
    val title: String,
    val url: String,
    val webview_url: String
)