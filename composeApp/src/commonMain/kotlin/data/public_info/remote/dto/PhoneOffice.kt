package data.public_info.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class PhoneOffice(
    val dialer: String,
    val visible: String
)