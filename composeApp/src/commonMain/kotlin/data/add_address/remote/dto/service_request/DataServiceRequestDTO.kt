package data.add_address.remote.dto.service_request

import domain.add_address.service_request.DataServiceRequest
import kotlinx.serialization.Serializable

@Serializable
data class DataServiceRequestDTO(
    val ticketId: Int
) {
    fun mapToDomain() : DataServiceRequest {
        return DataServiceRequest(
            ticketId = ticketId
        )
    }
}
