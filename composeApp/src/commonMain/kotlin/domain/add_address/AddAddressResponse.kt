package domain.add_address

data class AddAddressResponse(
    val data: Data
)

data class Data(
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
)