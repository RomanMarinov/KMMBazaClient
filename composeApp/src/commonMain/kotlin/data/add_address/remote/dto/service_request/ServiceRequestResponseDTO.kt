package data.add_address.remote.dto.service_request

import domain.add_address.service_request.ServiceRequestResponse
import kotlinx.serialization.Serializable

@Serializable
data class ServiceRequestResponseDTO(
    val data: DataServiceRequestDTO
) {
    fun mapToDomain() : ServiceRequestResponse{
        return ServiceRequestResponse(
            data = data.mapToDomain()
        )
    }
}
