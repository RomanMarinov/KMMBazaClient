package domain.repository

import domain.model.home.locations_internet_tv.DataLocations
import domain.model.home.tariffs_by_location.LocationTariffs
import domain.model.home.tariffs_by_location.LocationsTariffsBody
import kotlinx.coroutines.flow.Flow

interface HomeRepository {
    suspend fun getLocationsInternetTv() : Flow<List<DataLocations>>

    suspend fun getTariffsByLocation(body: LocationsTariffsBody) : LocationTariffs
}