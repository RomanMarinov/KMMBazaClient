package domain.add_address

data class CheckAddressData(
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
}