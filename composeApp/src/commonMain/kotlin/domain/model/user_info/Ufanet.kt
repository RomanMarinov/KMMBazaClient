package domain.model.user_info

import kotlinx.serialization.Serializable

@Serializable
data class Ufanet(
    val account: Account?,
    val addrId: Int,
    val flat: Int,
    val oper: String,
    val status: String,
    val title: String
)