package data.domofon.remote.model.dto.unlock

import domain.model.domofon.unlock.SputnikAnswer
import kotlinx.serialization.Serializable

@Serializable
data class SputnikAnswerDTO(
    val status: String
) {
    fun mapToDomain() : SputnikAnswer {
        return SputnikAnswer(
            status = status
        )
    }
}
