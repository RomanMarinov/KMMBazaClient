package data.auth.remote.dto.firebase

import domain.model.auth.firebase.DataOurServer
import kotlinx.serialization.Serializable

@Serializable
data class DataOurServerDTO(
    val result: Boolean
) {
    fun mapToDomain() : DataOurServer {
        return DataOurServer(
            result = result
        )
    }
}
