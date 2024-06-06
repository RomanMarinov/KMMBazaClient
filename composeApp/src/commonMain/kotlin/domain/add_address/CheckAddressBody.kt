package domain.add_address

import data.add_address.remote.dto.CheckAddressBodyDTO

data class CheckAddressBody(
    val text: String
) {
    fun mapToData() : CheckAddressBodyDTO {
        return CheckAddressBodyDTO(
            text = text
        )
    }
}
