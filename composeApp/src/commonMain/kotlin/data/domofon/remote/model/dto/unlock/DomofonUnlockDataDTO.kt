package data.domofon.remote.model.dto.unlock

import domain.model.domofon.unlock.DomofonUnlockData
import kotlinx.serialization.Serializable

@Serializable
data class DomofonUnlockDataDTO(
    val status: Boolean,
    val sputnikAnswer: SputnikAnswerDTO
) {
    fun mapToDomain() : DomofonUnlockData {
        return DomofonUnlockData(
            status = status,
            sputnikAnswer = sputnikAnswer.mapToDomain()
        )
    }
}
