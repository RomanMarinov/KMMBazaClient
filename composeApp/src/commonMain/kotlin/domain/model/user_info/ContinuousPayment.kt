package domain.model.user_info

import kotlinx.serialization.Serializable

@Serializable
data class ContinuousPayment(
    val canJoinToProgram: Boolean,
    val joined: Boolean,
    val speedBonus: Int,
    val startDate: String?
)