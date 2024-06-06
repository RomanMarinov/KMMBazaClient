package domain.model.user_info

import kotlinx.serialization.Serializable

@Serializable
data class DataX(
    val continuousPayment: ContinuousPayment?,
    val opDate: String?,
    val statusInt: Int?,
    val statusStr: String?,
    val tarif: Tarif?,
    val uid: Int,
    val oper: String
)