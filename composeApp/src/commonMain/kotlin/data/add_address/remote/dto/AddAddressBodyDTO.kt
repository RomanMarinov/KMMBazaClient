package data.add_address.remote.dto

import kotlinx.serialization.Serializable

@Serializable
class AddAddressBodyDTO(
    val addrId: Int,
    val oper: String,
    val address: String,
    val flat: String
)
