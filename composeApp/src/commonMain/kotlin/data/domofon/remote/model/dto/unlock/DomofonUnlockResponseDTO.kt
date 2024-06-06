package data.domofon.remote.model.dto.unlock

import domain.model.domofon.unlock.DomofonUnlockResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DomofonUnlockResponseDTO(
    //@SerialName("data")
    val data: DomofonUnlockDataDTO
) {
    fun mapToDomain() : DomofonUnlockResponse {
        return DomofonUnlockResponse(
            data = data.mapToDomain()
        )
    }
}
