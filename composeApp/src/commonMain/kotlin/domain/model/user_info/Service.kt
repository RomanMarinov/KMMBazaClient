package domain.model.user_info

import kotlinx.serialization.Serializable

@Serializable
data class Service(
    val address: Address,//
    val bonus: Int,
    val deposit: Double,// balance
    val oper: String,
    val payDate: String?,
    val price: Int,
    val recommendedPayment: Double,//  коплате
    val set: List<Set>?,
    val smsInvoice: Long,
    val statusInt: Int,
    val statusStr: String,
    val uid: Int
)