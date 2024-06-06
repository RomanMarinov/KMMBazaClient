package domain.model.user_info

import kotlinx.serialization.Serializable

@Serializable
data class AdditionalAddresses(
    val id: Int,
    val oper: String,
    val addrId: Int,
    val flat: Int,
    val verificationStatus: String,
    val city: String,
    val street: String,
    val home: String,
    val dvr: Boolean,
    val domofon: Boolean,
    val inet: Boolean,
    val ktv: Boolean
)

