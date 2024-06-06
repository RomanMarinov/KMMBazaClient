package data.add_address.remote.dto

import domain.add_address.AddAddressResponse
import domain.add_address.Data
import kotlinx.serialization.Serializable

@Serializable
data class AddAddressResponseDTO(
    val data: DataDTO
) {
    fun mapToDomain() : AddAddressResponse {
        return AddAddressResponse(
            data = data.mapToDomain()
        )
    }
}

@Serializable
data class DataDTO(
    val id: Int,
    val addrId: Int,
    val city: String,
    val flat: Int,
    val home: String,
    val oper: String,
    val street: String,
    val verificationStatus: String,
    val inet: Boolean,
    val ktv: Boolean,
    val domofon: Boolean,
    val dvr: Boolean
) {
    fun mapToDomain() : Data {
        return Data(
            id = id,
            addrId = addrId,
            city = city,
            flat = flat,
            home = home,
            oper = oper,
            street = street,
            verificationStatus = verificationStatus,
            inet = inet,
            ktv = ktv,
            domofon = domofon,
            dvr = dvr
        )
    }
}