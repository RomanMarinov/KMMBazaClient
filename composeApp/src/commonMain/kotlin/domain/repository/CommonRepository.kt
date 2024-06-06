package domain.repository

import data.public_info.remote.dto.Data


interface CommonRepository {

    suspend fun getPublicInfo() : Data
}