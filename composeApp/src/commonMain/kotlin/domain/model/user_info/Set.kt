package domain.model.user_info

import kotlinx.serialization.Serializable

@Serializable
data class Set(
    val data: DataX,
    val type: String
)