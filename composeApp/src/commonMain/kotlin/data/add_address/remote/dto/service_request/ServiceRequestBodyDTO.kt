package data.add_address.remote.dto.service_request

import kotlinx.serialization.Serializable
import util.Constants
@Serializable
data class ServiceRequestBodyDTO(
    val phone: String,
    val name: String? = null,
    val comment: String? = null,
    val city: String? = null,
    val street: String? = null,
    val home: String? = null,
    val flat: String? = null,
    val address: String? = null,
    val ref: Long? = null,
    val from: Int = Constants.ConnectionRequest.FROM,
    val inet: Boolean? = null,
    val ktv: Boolean? = null,
    val dvr: Boolean? = null,
    val domophone: Boolean? = null,
    val oper: String? = null,
    val uid: Int? = null
)