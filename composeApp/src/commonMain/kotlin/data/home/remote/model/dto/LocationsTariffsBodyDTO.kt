package data.home.remote.model.dto

import kotlinx.serialization.Serializable

@Serializable
data class LocationsTariffsBodyDTO(
    val locationId: Int,
    val oper: String
)
