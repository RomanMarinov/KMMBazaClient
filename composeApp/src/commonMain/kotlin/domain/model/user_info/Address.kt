package domain.model.user_info

import kotlinx.serialization.Serializable

@Serializable
data class Address(
    val addrId: Int,
    val city: String,
    val flat: String,
    val home: String,
    val street: String,
    val dvr: Boolean,
    val inet: Boolean,
    val ktv: Boolean,
    val domofon: Boolean,
)