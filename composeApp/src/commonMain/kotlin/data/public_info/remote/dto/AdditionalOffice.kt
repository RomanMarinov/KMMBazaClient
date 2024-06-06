package data.public_info.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class AdditionalOffice(
    val address: String,
    val phone: PhoneOffice,
    val worktime: String
)