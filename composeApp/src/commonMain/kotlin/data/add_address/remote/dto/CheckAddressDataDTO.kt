package data.add_address.remote.dto

import domain.add_address.CheckAddressData
import kotlinx.serialization.Serializable

@Serializable
data class CheckAddressDataDTO(
    val addr_id: Int,
    val city: String,
    val home: String,
    val inet: Int,
    val ktv: Int,
    val oper: String,
    val ready: String,
    val street: String,
    val tarif_group: String,
    val gps: String,
    val domofon: Boolean,
    val dvr: Boolean,
) {
    override fun toString(): String {
        return "$city, $street, $home"
    }

    fun mapToDomain() : CheckAddressData {
        return CheckAddressData(
            addr_id = addr_id,
            city = city,
            home = home,
            inet = inet,
            ktv = ktv,
            oper = oper,
            ready = ready,
            street = street,
            tarif_group = tarif_group,
            gps = gps,
            domofon = domofon,
            dvr = dvr
        )
    }
}