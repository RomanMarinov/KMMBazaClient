package data.add_address.remote.dto

import domain.add_address.CheckAddressResponse
import kotlinx.serialization.Serializable

@Serializable
data class CheckAddressResponseDTO(
    val data: List<CheckAddressDataDTO>?
) {
    fun mapToDomain() : CheckAddressResponse {
        return CheckAddressResponse(
            data = data?.map { it.mapToDomain() }
        )
    }
}