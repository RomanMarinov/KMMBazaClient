package data.add_address.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class CheckAddressBodyDTO(
    val text: String
)
