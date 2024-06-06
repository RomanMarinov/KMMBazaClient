package domain.model.home.tariffs_by_location

import data.home.remote.model.dto.LocationsTariffsBodyDTO

data class LocationsTariffsBody(
    val locationId: Int,
    val oper: String
) {
    fun mapToData() : LocationsTariffsBodyDTO {
        return LocationsTariffsBodyDTO(
            locationId = locationId,
            oper = oper
        )
    }
}