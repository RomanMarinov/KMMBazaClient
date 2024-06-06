package domain.repository

import domain.model.outdoor.Dvr

interface OutdoorRepository {
    suspend fun getOutdoors(): List<Dvr>
}