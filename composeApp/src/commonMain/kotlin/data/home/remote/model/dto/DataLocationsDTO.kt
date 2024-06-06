package data.home.remote.model.dto

import domain.model.home.locations_internet_tv.DataLocations
import kotlinx.serialization.Serializable

@Serializable
data class DataLocationsDTO(
    val id: Int,
    val name: String,
    val oper: String,
    val tarifGroup: String,
    val inet: Boolean,
    val ktv: Boolean
) {
    fun mapToDomain() : DataLocations {
        return DataLocations(
            id = id,
            name = name,
            oper = oper,
            tarifGroup = tarifGroup,
            inet = inet,
            ktv = ktv
        )
    }
}