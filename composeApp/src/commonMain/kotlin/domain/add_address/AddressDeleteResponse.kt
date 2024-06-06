package domain.add_address

data class AddressDeleteResponse(
    val data: DataDelete
)

data class DataDelete(
    val result: Boolean? = null
)
