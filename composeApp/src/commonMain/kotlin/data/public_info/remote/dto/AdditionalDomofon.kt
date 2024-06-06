package data.public_info.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class AdditionalDomofon(
    val domofonVendor: String,
    val location: String,
    val locationTitle: String
)