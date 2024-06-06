package data.add_address.remote.dto

import domain.add_address.AddressDeleteResponse
import domain.add_address.DataDelete
import kotlinx.serialization.Serializable

@Serializable
data class AddressDeleteResponseDTO(
    val data: DataDeleteDTO
) {
    fun mapToDomain() : AddressDeleteResponse {
        return AddressDeleteResponse(
            data = data.mapToDomain()
        )
    }
}

@Serializable
data class DataDeleteDTO(
    val result: Boolean
) {
    fun mapToDomain() : DataDelete {
        return DataDelete(
            result = result
        )
    }
}
