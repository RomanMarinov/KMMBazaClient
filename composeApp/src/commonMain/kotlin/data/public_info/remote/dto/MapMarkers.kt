package data.public_info.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class MapMarkers(
    val cityCams: CityCams,
    val domofonCams: DomofonCams,
    val officeCams: OfficeCams,
    val outdoorCams: OutdoorCams
)