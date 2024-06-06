package data.public_info.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class CompanyReview(
    val svgIcon: String,
    val title: String,
    val url: String
)