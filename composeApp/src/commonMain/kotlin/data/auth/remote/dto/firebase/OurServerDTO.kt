package data.auth.remote.dto.firebase

import domain.model.auth.firebase.OurServer
import kotlinx.serialization.Serializable

@Serializable
data class OurServerDTO(
    val data: DataOurServerDTO
) {
    fun mapToDomain() : OurServer {
        return OurServer(
            data = data.mapToDomain()
        )
    }
}
