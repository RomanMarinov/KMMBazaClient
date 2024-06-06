package domain.repository

import domain.model.domofon.unlock.DomofonUnlockResponse

interface DomofonRepository {
    suspend fun sendOpenDomofon(deviceId: String): DomofonUnlockResponse?
}