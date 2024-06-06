package data.public_info.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class PhoneContact(
    val dialer: String,
    val federal: String,
    val federalDialer: String,
    val visible: String
)