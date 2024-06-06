package data.history_call.remote.model.dto

import domain.model.history_call.HistoryCallResponse
import kotlinx.serialization.Serializable

@Serializable
data class HistoryCallResponseDTO(
    val data: List<HistoryCallAddressDTO>
) {
    fun mapToDomain() : HistoryCallResponse {
        return HistoryCallResponse(
            data = data.map { it.mapToDomain() }
        )
    }
}
