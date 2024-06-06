package domain.add_address.service_request

import data.add_address.remote.dto.service_request.ServiceRequestBodyDTO
import util.Constants

data class ServiceRequestBody(
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
) {
    fun mapToData() : ServiceRequestBodyDTO {
        return ServiceRequestBodyDTO(
            phone = phone,
            name = name,
            comment = comment,
            city = city,
            street = street,
            home = home,
            flat = flat,
            address = address,
            ref = ref,
            from = from,
            inet = inet,
            ktv = ktv,
            dvr = dvr,
            domophone = domophone,
            oper = oper,
            uid = uid
        )
    }
}