package data.public_info.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Support(
    val email: String,
    val messengers: Messengers,
    @SerialName("phone")
    val phoneContact: PhoneContact,
    val worktime: List<String>
)