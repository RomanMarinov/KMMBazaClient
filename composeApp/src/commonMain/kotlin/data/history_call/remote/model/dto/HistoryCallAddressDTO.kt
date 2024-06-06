package data.history_call.remote.model.dto

import domain.model.history_call.HistoryCallAddress
import kotlinx.serialization.Serializable

@Serializable
data class HistoryCallAddressDTO(
    val address: String,
    val answered: Boolean,
    val deviceID: String,
    val imageUrl: String,
    val time: String,
    val title: String,
    val streamerUrl: String,
    val token: String
) {
    fun mapToDomain() : HistoryCallAddress {
        return HistoryCallAddress(
            address = address,
            answered = answered,
            deviceID = deviceID,
            imageUrl = imageUrl,
            time = time,
            title = title,
            streamerUrl = streamerUrl,
            token = token
        )
    }
}
