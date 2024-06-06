package domain.model.user_info

import kotlinx.serialization.Serializable

@Serializable
data class Credit(
    val description: String,
    val monthlyPayment: Int,
    val oper: String,
    val payDate: String,
    val remain: Int,
    val startDate: String,
    val uid: Int
)